sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/ui/model/json/JSONModel",
    "sap/m/MessageToast"
], function (Controller, JSONModel, MessageToast) {
    "use strict";

    return Controller.extend("repository.secret.ui.controller.App", {
        
        // base REST API controller
        baseUrl: "http://localhost:8081/api/v1",

        get secretsUrl() { return this.baseUrl + "/secrets"; },
        get reposUrl() { return this.baseUrl + "/repositories"; },
        get validationUrl() { return this.baseUrl + "/validate"; },

        onInit: function () {
            // Initialize an empty JSON model (data will be loaded from the backend database)
            var oModel = new JSONModel({
                secrets: [],
                repos: []
            });
            this.getView().setModel(oModel);

            // Fetch live data from the Spring Boot backend
            this._loadSecrets();
            this._loadRepos();
        },

       /**
         * Fetches all secrets and separates the data into two model paths:
         * One clean array for the table view, and one containing a default "Public" option for the dropdown list.
         */
        _loadSecrets: function () {
            var oModel = this.getView().getModel();

            fetch(this.secretsUrl)
                .then(function (oResponse) {
                    if (!oResponse.ok) {
                        throw new Error("HTTP error! Status: " + oResponse.status);
                    }
                    return oResponse.json();
                })
                .then(function (aData) {
                    var aRawSecrets = (aData && Array.isArray(aData)) ? aData : [];

				   // 1. Path for the Secrets tab table (contains only DB records)
                    oModel.setProperty("/secrets", aRawSecrets);

                    // 2. Separate deep copy path specifically for the Repo creation dropdown select control
                    var aDropdownData = JSON.parse(JSON.stringify(aRawSecrets));
                    
                    // Prepend the Public option exclusively to the dropdown data set
                    aDropdownData.unshift({
                        id: "",
                        name: "-- Public (No Secret) --"
                    });

                    // Update the isolated dropdown model path
                    oModel.setProperty("/dropdownSecrets", aDropdownData);

                }.bind(this))
                .catch(function (oError) {
                    oModel.setProperty("/secrets", []);
                    oModel.setProperty("/dropdownSecrets", [{ id: "", name: "-- Public (No Secret) --" }]);
                    MessageToast.show("Error loading secrets: " + oError.message);
                });
        },
        /*
         * Sends a new secret object payload to the backend
         */
       onAddSecret: function () {
            var sName = this.byId("inpSecretName").getValue().trim();
            var sProvider = this.byId("selProviderType").getSelectedKey();
            
            // Matches your logic: GITHUB -> specific type, otherwise GENERIC
            var sSpecificType = (sProvider === "GITHUB") ? this.byId("selGithubType").getSelectedKey() : "GENERIC";

            var sValue = "";
            if (this.byId("inpSecretKey").getVisible()) {
                sValue = this.byId("inpSecretKey").getValue().trim(); 
            } else {
                sValue = this.byId("inpSecretToken").getValue().trim(); 
            }

            if (!sName || !sValue || !sProvider) {
                sap.m.MessageToast.show("Please fill all required fields.");
                return;
            }

            // FIX: This object MUST look exactly like your current Java Entity class fields!
            var oNewSecret = {
                name: sName,
                secretValue: sValue,
                provider: sProvider,  
                secretType: sSpecificType
            };

            fetch(this.secretsUrl, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(oNewSecret) // Serializes the exact object
            })
            .then(function (oResponse) {
                if (!oResponse.ok) {
                    throw new Error("Failed to save secret to backend.");
                }
                return oResponse.json();
            })
            .then(function (oData) {
                sap.m.MessageToast.show("Secret added successfully.");

                // Clear input forms
                this.byId("inpSecretName").setValue("");
                this.byId("inpSecretToken").setValue("");
                this.byId("inpSecretKey").setValue("");

                // Refresh model/table
                this._loadSecrets();
            }.bind(this))
            .catch(function (oError) {
                sap.m.MessageToast.show("Creation error: " + oError.message);
            });
        },

        /**
         * Removes a secret record from the backend using its ID
         */
        onDeleteSecret: function (oEvent) {
			// Get the binding context path for the specific table row clicked
            var oBindingContext = oEvent.getSource().getBindingContext();
            
            // Extract the technical ID generated by the database
            var sSecretId = oBindingContext.getProperty("id"); 

            // Send the DELETE request to the specific endpoint resource: /api/v1/secrets/{id}
            fetch(this.secretsUrl + "/" + sSecretId, {
                method: "DELETE"
            })
            .then(function (oResponse) {
                if (!oResponse.ok) {
                    throw new Error("Failed to delete secret from backend.");
                }
                MessageToast.show("Secret deleted successfully.");
                
                // Refresh the table UI data directly from the database
                this._loadSecrets();
            }.bind(this))
            .catch(function (oError) {
                MessageToast.show("Deletion error: " + oError.message);
            });
        },
        /**
         * Fetches all Repos from the database via REST API
         */
        _loadRepos: function () {
            var oModel = this.getView().getModel();
            
            fetch(this.reposUrl)
                .then(oResponse => {
                    if (!oResponse.ok) throw new Error("HTTP error! Status: " + oResponse.status);
                    return oResponse.json();
                })
                .then(aData => {
                    oModel.setProperty("/repos", Array.isArray(aData) ? aData : []);
                    oModel.refresh(true); 
                })
                .catch(oError => {
                    oModel.setProperty("/repos", []);
                    MessageToast.show("Error loading repositories: " + oError.message);
                });
        },
       /**
         * Handles the entry of a new repository, posts it to the backend, 
         * and updates the local frontend UI model right away.
         */
         onAddRepo: function () {
            // Read the new Name field
            var sName = this.byId("inpRepoName").getValue().trim();
            var sUrl = this.byId("inpRepoUrl").getValue().trim();
            var oSelect = this.byId("selSecret");
            var sSecretId = oSelect.getSelectedKey();
            
 			// Safer way to fetch the text from the currently selected item		
            var oSelectedItem = oSelect.getSelectedItem();
            var sSecretName = "";
            
            if (oSelectedItem) {
                sSecretName = oSelectedItem.getText();
            }

            // Validate that both Name and URL are provided
            if (!sName || !sUrl) {
                sap.m.MessageToast.show("Please enter both Repository Name and URL.");
                return;
            }

            // Include the name in the payload
            var oPayload = {
                name: sName,
                url: sUrl,
                secretId: sSecretId ? parseInt(sSecretId) : null
            };

            fetch(this.reposUrl, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(oPayload)
            })
            .then(function (oResponse) {
                if (!oResponse.ok) {
                    throw new Error("Failed to save repository to backend.");
                }
                return oResponse.json();
            })
            .then(function (iNewInsertedId) {
                sap.m.MessageToast.show("Repository added successfully.");

                // Clear all input fields
                this.byId("inpRepoName").setValue("");
                this.byId("inpRepoUrl").setValue("");
                this.byId("selSecret").setSelectedKey(""); 

                this._loadRepos();
            }.bind(this))
            .catch(function (oError) {
                sap.m.MessageToast.show("Creation error: " + oError.message);
            });
        },
        /**
         * Handles the event when a user deletes an existing repository.
         * Sends a DELETE request to the backend using the record's database ID.
         */
        onDeleteRepo: function (oEvent) {
            var oBindingContext = oEvent.getSource().getBindingContext();
            var sRepoId = oBindingContext.getProperty("id"); 

            fetch(this.reposUrl + "/" + sRepoId, {
                method: "DELETE"
            })
            .then(function (oResponse) {
                if (!oResponse.ok) {
                    throw new Error("Failed to delete repository from backend.");
                }
                MessageToast.show("Repository deleted successfully.");
                this._loadRepos();
            }.bind(this))
            .catch(function (oError) {
                MessageToast.show("Deletion error: " + oError.message);
            });
        },

        /**
         * Triggered when the user changes the Provider (e.g., from Other to GitHub)
         */
        onProviderChange: function (oEvent) {
            var sProvider = oEvent.getParameter("selectedItem").getKey();
            var oBoxGithubType = this.byId("boxGithubType");

            if (sProvider === "GITHUB") {
                // Show the dropdown for GitHub secret types
                oBoxGithubType.setVisible(true);
                // Manually trigger the type change logic to adjust the fields based on the current selection
                this.onGithubTypeChange(); 
            } else {
                // Hide the GitHub dropdown and restore the standard Token input field
                oBoxGithubType.setVisible(false);
                this.byId("inpSecretToken").setVisible(true);
                this.byId("inpSecretKey").setVisible(false);
            }
        },

        /**
         * Triggered when the user selects a specific GitHub secret type
         */
        onGithubTypeChange: function () {
            var sGithubType = this.byId("selGithubType").getSelectedKey();
            var oInputToken = this.byId("inpSecretToken");
            var oTextAreaKey = this.byId("inpSecretKey");

            if (sGithubType === "PAT") {
                // PAT is a short string - use an Input field
                oInputToken.setVisible(true);
                oTextAreaKey.setVisible(false);
            } else if (sGithubType === "SSH" || sGithubType === "APP") {
                // PAT is a short string - use an Input field
                oInputToken.setVisible(false);
                oTextAreaKey.setVisible(true);
            }
        },

        formatRepoName: function (sName) {
            if (sName) {
                return sName;
            }
            return "Public repo";
        },

        formatRepoState: function (sName) {
            if (sName) {
                return "Success";
            }
            return "None";
        },

        onValidateRepo: function (oEvent) {
            var oBindingContext = oEvent.getSource().getBindingContext();
            var oRepo = oBindingContext.getObject();

            if (!oRepo.secretId) {
                sap.m.MessageToast.show("Public repository - no validation required.");
                return;
            }

            var oPayload = {
                repoUrl: oRepo.url,
                secretId: oRepo.secretId
            };

            sap.m.MessageToast.show("Validating... Please wait.");

            fetch(this.validationUrl, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(oPayload)
            })
            .then(function (oResponse) {
                if (!oResponse.ok) {
                    throw new Error("Validation request failed.");
                }
                return oResponse.json();
            })
            .then(function (oData) {
                if (oData.valid) {
                    sap.m.MessageToast.show("Success: Repository credentials are VALID.");
                } else {
                    sap.m.MessageToast.show("Error: INVALID credentials. " + (oData.message || ""));
                }
            })
            .catch(function (oError) {
                sap.m.MessageToast.show("Validation connection error: " + oError.message);
            });
        },
        //EDIT REPO
        // 1. Triggered when clicking the Edit button (the pencil icon)
        onEditRepo: function (oEvent) {
            var oContext = oEvent.getSource().getBindingContext();
            
            // Create a shallow/deep copy of current data in case the user cancels the action
            this._oOriginalRepoData = Object.assign({}, oContext.getObject());
            
            // Activate edit mode only for this specific row
            oContext.getModel().setProperty(oContext.getPath() + "/editable", true);
        },

        // 2. Triggered when clicking the Cancel button
        onCancelEditRepo: function (oEvent) {
            var oContext = oEvent.getSource().getBindingContext();
            var oModel = oContext.getModel();
            var sPath = oContext.getPath();

            // Restore the original values from before the edit session started
            oModel.setProperty(sPath + "/name", this._oOriginalRepoData.name);
            oModel.setProperty(sPath + "/url", this._oOriginalRepoData.url);
            oModel.setProperty(sPath + "/secretId", this._oOriginalRepoData.secretId);
            
            // Lock the row back into read-only mode
            oModel.setProperty(sPath + "/editable", false);
        },

        // 3. Triggered when clicking the Save button (the diskette icon)
        onSaveRepo: function (oEvent) {
            var oContext = oEvent.getSource().getBindingContext();
            var oModel = oContext.getModel();
            var sPath = oContext.getPath();
            var oEditedRepo = oContext.getObject(); // Retrieve the updated data from the model

            // Request to your backend controller for the update (usually PUT or POST)
            fetch(this.reposUrl +"/"+ oEditedRepo.id, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    name: oEditedRepo.name,
                    url: oEditedRepo.url,
                    secretId: oEditedRepo.secretId // Send the new secret ID if it was changed
                })
            })
            .then(function (response) {
                if (response.ok) {
                    sap.m.MessageToast.show("Repository updated successfully!");
                    
                    // Important: You can either reload the entire list from the backend here or manually update the secretName on screen
                    
                    // Lock the row back into read-only mode
                    oModel.setProperty(sPath + "/editable", false);
                } else {
                    sap.m.MessageToast.show("Failed to update repository.");
                }
            })
            .catch(function (error) {
                sap.m.MessageToast.show("Error connecting to server: " + error);
            });
        },
    });
});
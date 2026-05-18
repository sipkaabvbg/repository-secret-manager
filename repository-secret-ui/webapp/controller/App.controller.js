sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/ui/model/json/JSONModel"
], function (Controller, JSONModel) {
    "use strict";

    return Controller.extend("repository.secret.ui.controller.App", { 
        onInit: function () {
            var oData = {
                secrets: [
                    { id: "1", name: "GitHub Personal Access Token" },
                    { id: "2", name: "GitLab Deployment Key" }
                ],
                repos: [],
                newSecret: { name: "", value: "" },
                newRepo: { url: "", secretId: "" }
            };

            var oModel = new JSONModel(oData);
            this.getView().setModel(oModel); 
        },

        onAddSecret: function() {
            // 
        },

        onDeleteSecret: function() {
            // 
        }
    });
});
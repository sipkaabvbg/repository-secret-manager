sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/ui/model/json/JSONModel"
], function (Controller, JSONModel) {
    "use strict";
    return Controller.extend("repository.secret.ui.Component", {
        onInit: function () {
           
            var oData = {
                secrets: [],
                repos: [],
                newSecret: { name: "", value: "" },
                newRepo: { url: "", secretId: "" }
            };
            this.getView().setModel(new JSONModel(oData));
        }
        
    });
});
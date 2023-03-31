const {defineConfig} = require('cypress');

module.exports = defineConfig({
    e2e: {
        'baseUrl': 'http://localhost:8080',
        'viewportWidth': 1100,
        'viewportHeight': 800,
        "reporter": "cypress-multi-reporters",
        "reporterOptions": {
            "configFile": "reporter-config.json"
        },
        setupNodeEvents(on, config) {
            // implement node event listeners here
        },
    },
});

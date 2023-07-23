const { defineConfig } = require("cypress");

module.exports = defineConfig({
  projectId: 'fz1rz9',
  e2e: {
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
  },
});

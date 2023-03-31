/// <reference types="Cypress" />

describe('User management', () => {
    beforeEach(() => {
        cy.setCookie('org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE', 'en');
        cy.request({
            method: 'POST',
            url: 'api/integration-test/reset-db',
            followRedirect: false
        }).then((response) => {
            expect(response.status).to.eq(200);
        });
        cy.loginByForm('admin.strator@gmail.com', 'admin-pwd'); //<.>
        cy.visit('/users'); //<.>
    });

    it('should be possible to navigate to the Add User form', () => {
        cy.get('#add-user-button').click(); //<.>

        cy.url().should('include', '/users/create'); //<.>
    });
});

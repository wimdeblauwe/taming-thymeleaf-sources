/// <reference types="Cypress" />

describe('Team management', () => {
    beforeEach(() => { //<.>
        cy.setCookie('org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE', 'en');
        cy.request({
            method: 'POST',
            url: 'api/integration-test/reset-db',
            followRedirect: false
        }).then((response) => {
            expect(response.status).to.eq(200);
        });
        cy.loginByForm('admin.strator@gmail.com', 'admin-pwd');
        cy.visit('/teams');
    });

    it('should be possible to add a team', () => { //<.>
        cy.get('#add-team-button').click();

        cy.url().should('include', '/teams/create');

        cy.get('#name').type('Wizards'); //<.>
        cy.get('#coachId').select('Admin Strator'); //<.>
        cy.get('#submit-button').click(); //<.>

        cy.get('#teams-table').find('tbody tr').should('have.length', 1); //<.>
    });

    // tag::delete[]
    it('should be possible to delete a team', () => {
        cy.request({ //<.>
            method: 'POST',
            url: 'api/integration-test/add-test-team',
            followRedirect: false
        }).then((response) => {
            expect(response.status).to.eq(200);
        });

        // We should have 1 team to get started
        cy.visit('/teams'); //<.>
        cy.get('#teams-table').find('tbody tr').should('have.length', 1);

        cy.get('[id^=delete-link-]').click(); //<.>
        cy.get('#delete-modal-message').contains('Are you sure you want to delete team Test Team?'); //<.>
        cy.get('#delete-modal-submit-button').click(); //<.>

        // There should be no team left after the delete
        cy.get('#teams-table').find('tbody tr').should('have.length', 0); //<.>

        cy.get('#success-alert-message').contains('Team Test Team was deleted successfully.'); //<.>

        cy.reload(); //<.>
        cy.get('#success-alert-message').should('not.exist'); //<.>
    });
    // end::delete[]
});

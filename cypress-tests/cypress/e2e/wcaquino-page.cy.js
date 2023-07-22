describe('Test page wcaquino.me', () => {
  beforeEach(() => {
    cy.visit('https://wcaquino.me/cypress/componentes.html');
  });

  it('Click button and validate value', () => {
    cy.get('#buttonSimple').should('contain.value', 'Clique Me!');
    cy.get('#buttonSimple').click();
    cy.get('#buttonSimple').should('contain.value', 'Obrigado!');
  });

  it('Click button and validate value', () => {
    cy.get('#buttonSimple').should('contain.value', 'Clique Me!');
    cy.get('#buttonSimple').click();
    cy.get('#buttonSimple').should('contain.value', 'Obrigado!');
  });
})
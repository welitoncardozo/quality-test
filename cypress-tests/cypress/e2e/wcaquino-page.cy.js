describe('Test page wcaquino.me, this is only demo', () => {
  beforeEach(() => {
    cy.visit('https://wcaquino.me/cypress/componentes.html');
  });

  it('Click button and validate value', () => {
    cy.get('#buttonSimple').should('contain.value', 'Clique Me!');
    cy.get('#buttonSimple').click();
    cy.get('#buttonSimple').should('contain.value', 'Obrigado!');
  });

  it('Fill form', () => {
    cy.get('#resultado').should('contain.text', 'Status: Nao cadastrado');

    cy.get('#formNome').should('be.visible');
    cy.get('#formNome').type('Weliton');
    cy.get('[data-cy="dataSobrenome"]').type('Cardozo');
    cy.get('#formSexoMasc').check();
    cy.get('#formCadastrar').click();

    cy.get('#descNome').should('contain.text', 'Weliton');
    cy.get('#descSobrenome').should('contain.text', 'Cardozo');
    cy.get('#descSexo').should('contain.text', 'Masculino');
  });
});
describe('Add alexa to shopping cart', () => {
  it('Add alexa to shopping cart by search without login', () => {
    cy.visit('https://amazon.com.br');

    cy.get('#twotabsearchtextbox').type('Alexa');
    cy.get('#nav-search-submit-button').click();

    cy.get('[data-index="0"]').should('be.visible');
    cy.get('[data-index="0"]').should('contain.text', 'O preço e outros detalhes variam de acordo com o tamanho e a cor do produto.');
    cy.get('[data-index="2"]').should('be.visible');
    cy.get('[data-index="2"]').click();

    cy.get('#add-to-cart-button').click();
    cy.get('[id="desktop-ptc-button-celWidget"]').click();

    cy.get('.a-spacing-small').should('contain.text', 'Fazer login');
    cy.get('#createAccountSubmit').should('contain.text', 'Criar sua conta da Amazon');
  });
});
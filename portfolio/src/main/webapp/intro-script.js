/**
 * Adds a random fact to the page.
 */
function addRandomFact() {
    let min = 0;
    let previous = -1;
    const facts =
      ['I love food!', 'I have just learned CSS.', 'I like photography.', 'My favorite cartoon is Phineas and Ferb.'];
    let max = facts.length-1;
    return function() {
        var value = Math.floor(Math.random() * (max - min + 1)) + min;
        while (value === previous) {
            value = Math.floor(Math.random() * (max - min + 1)) + min;
        }
        previous = value;
        let fact = facts[value];
        const factContainer = document.getElementById('fact-container');
        factContainer.innerText = fact;
    };
    

}
let fact = addRandomFact();

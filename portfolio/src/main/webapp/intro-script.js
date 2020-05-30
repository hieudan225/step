// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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

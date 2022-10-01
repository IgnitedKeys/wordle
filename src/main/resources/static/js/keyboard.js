
const Keyboard = {
    elements: {
        main: null,
        keysContainer: null,
        keys: []
    },

    eventHandlers: {
        oninput: null
    },

    properties: {
        value: ""
    },

    init() {
        //create main elements
        this.elements.main = document.createElement("div");
        this.elements.keysContainer = document.createElement("div");

        //setup main elements
        this.elements.main.classList.add("keyboard");
        this.elements.keysContainer.classList.add("keyboard__keys");
        this.elements.keysContainer.appendChild(this._createKeys());

        //add to DOM
        this.elements.main.appendChild(this.elements.keysContainer);
        document.body.appendChild(this.elements.main);
    },

    _createKeys() {
        const fragment = document.createDocumentFragment();
        const keyLayout = [
            "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
            "A", "S", "D", "F", "G", "H", "J", "K", "L",
            "ENTER", "Z", "X", "C", "V", "B", "N", "M", "backspace"
        ];

        //create HTML for icon
        const createIconHTML = (icon_name) => {
            return `<i class="material-icons">${icon_name}</i>`;
        };

        keyLayout.forEach(key => {
            const keyElement = document.createElement("button");
            const insertLineBreak = ["P", "L"].indexOf(key) !== -1;

            keyElement.setAttribute("type", "button");
            keyElement.classList.add("keyboard__key");

            switch (key) {
                case "backspace":
                    keyElement.classList.add("keyboard__key--wide");
                    keyElement.innerHTML = createIconHTML("backspace");

                    keyElement.addEventListener("click", () => {
                        this.properties.value = this.properties.value.substring(0, this.properties.value.length - 1);
                        this._triggerEvent("oninput");
                    });
                    break;

                case "ENTER":
                    keyElement.classList.add("keyboard__key--wide");
                    keyElement.textContent = "ENTER";

                    keyElement.addEventListener("click", () => {
                        this.properties.value += "/n";
                        this._triggerEvent("oninput");
                    });
                    break;
                
                default: 
                    keyElement.textContent = key;
                    
                    keyElement.addEventListener("click", ()=> {
                       this.properties.value += key.lowerCase();
                       this._triggerEvent("oninput");
                    });
                    break; 
            }
            
            fragment.appendChild(keyElement);
            
            if(insertLineBreak) {
                fragment.appendChild(document.createElement("br"));
            }
        });
        
        return fragment;
    },

    _triggerEvent(handlerName) {
        console.log("event triggered. event name: " + handlerName);
    },

    open(initialValue, oninput) {

    }

};

window.addEventListener("DOMContentLoaded", function () {
    Keyboard.init();
});

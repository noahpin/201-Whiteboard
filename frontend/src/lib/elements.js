export class Element {
    /**
     * base class for elements
     * all elements include a string that defines the type
     * it then has a content variable, which will be different depending on the type of element
     * it will then have a properties variable, which baseline for all elements has width, height, x, y, fillColor, borderColor, and textColor
     * 
     * @typedef {Object} Element
     * @property {string} type - the type of the element (e.g., 'text', 'image', etc.)
     * @property {any} content - the content of the element, which can vary based on the type
     * @property {Object} properties - the properties of the element, which include baseline attributes like width, height, x, y, fillColor, borderColor, and textColor
     * 
     * 
     */

    constructor(type, content, properties = {}) {
        this.type = type;
        this.content = content;
        /**
         * properties of the element
         * @type {Object}
         */
        this.properties = {
            width: properties.width || 100, // default width
            height: properties.height || 100, // default height
            x: properties.x || 0, // default x position
            y: properties.y || 0, // default y position
        };
    }

    updatePosition(x, y) {
        /**
         * update the position of the element
         * @param {number} x - the new x coordinate
         * @param {number} y - the new y coordinate
         */
        this.properties.x = x;
        this.properties.y = y;
    }
    updateSize(width, height) {
        /**
         * update the size of the element
         * @param {number} width - the new width
         * @param {number} height - the new height
         */
        this.properties.width = width;
        this.properties.height = height;
    }

    export() {
        /**
         * export the element as a JSON object
         * this will be used to serialize the element for storage or transmission
         */
        return {
            type: this.type,
            content: this.content,
            properties: this.properties
        };
    }

}

export class TextElement extends Element {
    /**
     * TextElement class that extends the base Element class
     * 
     * @param {string} content - the text content of the element
     * @param {Object} properties - additional properties for the element
     */
    constructor(content, properties) {
        super('text', content, properties);
    }
    updateTextContent(text) {
        /**
         * update the text content of the element
         * @param {string} text - the new text content
         */
        this.content = text;
    }
}

export class PenElement extends Element {

    /**
     * BrushElement class that extends the base Element class
     * 
     * @param {string} content - the brush content of the element
     * @param {Object} properties - additional properties for the element
     */
    constructor(content, properties) {
        super('pen', content, properties);
        this.path = content?.path ?? [];
        this.pathUpdateListeners = [];
        this.onPathUpdated = ()=>{};
    }
    /**
     * add a point to the path of the pen element
     * @param {number} x - the LOCAL x coordinate of the point
     * @param {number} y - the LOCAL y coordinate of the point
     */
    addPointToPath(x, y) {
        this.path.push([x, y]);
        this.renderPath();
        /**
         * notify all listeners that the path has been updated
         */
        this.onPathUpdated();
    }
    renderPath() {

    }

    export() {
        this.content = {path: this.path};
        return super.export();
    }
}
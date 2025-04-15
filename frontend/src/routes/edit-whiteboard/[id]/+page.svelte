<!-- this is where the meat of the editing should be
 i (noah) am imagining that the canvas/whiteboard would probably be
 split off into its own component and then beyond that every
 child element will as well. -->

<script>
	import TextElementComponent from "$lib/components/TextElementComponent.svelte";
	import { TextElement } from "$lib/elements";

	let whiteboardElements = [];
    let currentTool = "pen"
	whiteboardElements.push(new TextElement());
    let currentlyModifyingElement = null;

    let panX = $state(0);
    let panY = $state(0);
    function pointerDownHandler(e) {
        e.preventDefault();
        if (e.target.classList.contains("whiteboard")) {
            let newElement;
            switch (currentTool) {
                case "text":
                newElement = (new TextElement());
                newElement.updatePosition(
                    e.clientX - panX,
                    e.clientY - panY)
                    break;
                case "pen":
                    break;
                default:
                    break;
            }
            whiteboardElements.push(newElement);
        }
    }
    function pointerUpHandler(e) {
        e.preventDefault();
        
    }
    function pointerMoveHandler(e) {
        e.preventDefault();
    }
    function wheelHandler(e) {
        e.preventDefault();
        if(e.ctrlKey )return;
        panX -= e.deltaX || 0;
        panY -= e.deltaY || 0;
    }
    function addTextElementHandler(){
        
    }
    function addBrushStrokeHandler(){

    }
    function addEraserHandler(){

    }
</script>

<svelte:body
on:pointerdown|nonpassive={pointerDownHandler}
on:pointerup|nonpassive={pointerUpHandler}
on:pointermove|nonpassive={pointerMoveHandler}
on:wheel|nonpassive={wheelHandler}></svelte:body>

<div class="whiteboard" style:background-position={panX + "px " + panY + "px"}>

{#each whiteboardElements as element, index}
	<div>
        {#if element.type === "text"}
            <TextElementComponent {panX} {panY} bind:elementX={whiteboardElements[index].properties.x} bind:elementY={whiteboardElements[index].properties.y} bind:elementData={whiteboardElements[index]} />
        {/if}
	</div>
{/each}


</div>

<div class="toolbar">
    <button on:click={addTextElementHandler}> aria-label="Add Text" 💬</button> 
    <button on:click={addBrushStrokeHandler}> aria-label="Brush Stroke" 🖌️</button>
    <button on:click={addEraserHandler}> aria-label="Eraser" ⌫</button>
</div>

<style>
    .whiteboard {
        width: 100%;
        height: 100%;
  background-image: url('data:image/svg+xml;utf8,<svg width="40" height="40" viewBox="0 0 40 40" xmlns="http://www.w3.org/2000/svg"><circle cx="1" cy="1" r="1" fill="%23ccc"/></svg>');
  background-repeat: repeat;

}

    .toolbar{
        width: 100%;
        height: 10%;
    }
</style>


<!-- this is where the meat of the editing should be
 i (noah) am imagining that the canvas/whiteboard would probably be
 split off into its own component and then beyond that every
 child element will as well. -->

<script>
	import TextElementComponent from "$lib/components/TextElementComponent.svelte";
    import PenElementComponent from "$lib/components/PenElementComponent.svelte";
	import { TextElement, PenElement } from "$lib/elements";


	let whiteboardElements = $state([]);
    let currentTool = "text"
	whiteboardElements.push(new TextElement());
    let currentlyModifyingElement = null;
    let clickDown = $state(false);
    let panX = $state(0);
    let panY = $state(0);
    let penOriginalX = 0;
    let penOriginalY = 0;
    function pointerDownHandler(e) {
        e.preventDefault();
        clickDown = true;
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
                    newElement = (new PenElement());
                    newElement.updatePosition(
                        e.clientX - panX,
                        e.clientY - panY)
                    newElement.addPointToPath(
                        0,
                        0)
                    penOriginalX = e.clientX - panX;
                    penOriginalY = e.clientY - panY;
                    currentlyModifyingElement = newElement;
                    break;
                default:
                    break;
            }
            whiteboardElements.push(newElement);
        }
    }
    function pointerUpHandler(e) {
        e.preventDefault();
        clickDown = false;
        currentlyModifyingElement = null;
        
    }
    function pointerMoveHandler(e) {
        e.preventDefault();
        if (clickDown) {
            if (currentlyModifyingElement != null && currentlyModifyingElement.type == "pen") {
                currentlyModifyingElement.addPointToPath(
                    e.clientX - panX - penOriginalX,
                    e.clientY - panY - penOriginalY)
            }
        }
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
        {:else if element.type == "pen"}
            <PenElementComponent {panX} {panY} bind:elementX={whiteboardElements[index].properties.x} bind:elementY={whiteboardElements[index].properties.y} bind:elementData={whiteboardElements[index]} />
        {/if}
	</div>
{/each}


</div>

<div class="toolbar">
    <button on:click={addTextElementHandler}> Add TextBox 💬</button> 
    <button on:click={addBrushStrokeHandler}> Brush Stroke 🖌️</button>
    <button on:click={addEraserHandler}> Erase ⌫</button>
</div>

<style>
    .whiteboard {
        width: 100%;
        height: calc(100vh - 50px);
  background-image: url('data:image/svg+xml;utf8,<svg width="40" height="40" viewBox="0 0 40 40" xmlns="http://www.w3.org/2000/svg"><circle cx="1" cy="1" r="1" fill="%23ccc"/></svg>');
  background-repeat: repeat;

}

    .toolbar {
        position: fixed;
        width: 100%;
        height: 50px;
        background-color:rgb(169, 208, 255);
        border-bottom: 1px solid #ccc;
        display: flex;
        align-items: center;
        padding: 0 10px;
        gap: 10px;
    }

    .toolbar button {
        padding: 6px 12px;
        font-size: 14px;
        cursor: pointer;
    }

</style>


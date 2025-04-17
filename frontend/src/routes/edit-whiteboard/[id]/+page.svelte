<!-- this is where the meat of the editing should be
 i (noah) am imagining that the canvas/whiteboard would probably be
 split off into its own component and then beyond that every
 child element will as well. -->

<script>
	import TextElementComponent from "$lib/components/TextElementComponent.svelte";
	import PenElementComponent from "$lib/components/PenElementComponent.svelte";
	import { TextElement, PenElement } from "$lib/elements";

	let whiteboardElements = $state([]);
	let currentTool = "text";
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
			switch (currentTool) {
				case "text":
					let textElement = new TextElement();
					textElement.updatePosition(e.clientX - panX, e.clientY - panY);
					whiteboardElements.push(textElement);
					break;
				case "pen":
					let penElement = new PenElement();
					penElement.updatePosition(e.clientX - panX, e.clientY - panY);
					penElement.addPointToPath(0, 0);
					penOriginalX = e.clientX - panX;
					penOriginalY = e.clientY - panY;
					currentlyModifyingElement = penElement;
                    whiteboardElements.push(penElement);
					break;
				default:
					break;
			}
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
			if (
				currentlyModifyingElement != null &&
				currentlyModifyingElement.type == "pen"
			) {
				currentlyModifyingElement.addPointToPath(
					e.clientX - panX - penOriginalX,
					e.clientY - panY - penOriginalY
				);
			}
		}
	}
	function wheelHandler(e) {
		e.preventDefault();
		if (e.ctrlKey) return;
		panX -= e.deltaX || 0;
		panY -= e.deltaY || 0;
	}
	function addTextElementHandler() {
		currentTool = "text";
	}
	function addBrushStrokeHandler() {
		currentTool = "pen";
	}
	function addEraserHandler() {
		currentTool = "erase";
	}
</script>

<svelte:body
	on:pointerdown|nonpassive={pointerDownHandler}
	on:pointerup|nonpassive={pointerUpHandler}
	on:pointermove|nonpassive={pointerMoveHandler}
	on:wheel|nonpassive={wheelHandler}
/>

<div class="whiteboard" style:background-position={panX + "px " + panY + "px"}>
	{#each whiteboardElements as element, index}
		<div>
			{#if element.type === "text"}
				<TextElementComponent
					{panX}
					{panY}
					bind:elementData={whiteboardElements[index]}
				/>
			{:else if element.type == "pen"}
				<PenElementComponent
					{panX}
					{panY}
					bind:elementData={whiteboardElements[index]}
				/>
			{/if}
		</div>
	{/each}
</div>

<div class="toolbar">
	<button onclick={addTextElementHandler}> Add TextBox 💬</button>
	<button onclick={addBrushStrokeHandler}> Brush Stroke 🖌️</button>
	<button onclick={addEraserHandler}> Erase ⌫</button>
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
		background-color: rgb(254, 207, 239);
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

<!-- this is where the meat of the editing should be
 i (noah) am imagining that the canvas/whiteboard would probably be
 split off into its own component and then beyond that every
 child element will as well. -->

<script>
	import TextElementComponent from "$lib/components/TextElementComponent.svelte";
	import PenElementComponent from "$lib/components/PenElementComponent.svelte";
	import { TextElement, PenElement } from "$lib/elements";
	import { onMount, onDestroy } from "svelte";
	import { PUBLIC_LOCALHOST_URL } from "$env/static/public";
	import { deleteCookie, getCookie, setCookie } from "svelte-cookie";
	// import { load } from "../+page.server.js";

	let { data } = $props();
	console.log(data);

	let loggedIn = $state(false);

	let whiteboardElements = $state([]);
	let currentTool = $state("text");
	let currentlyModifyingElement = null;
	let clickDown = $state(false);
	let panX = $state(0);
	let panY = $state(0);
	let penOriginalX = 0;
	let penOriginalY = 0;
	let guestMode = $state(false)
	function pointerDownHandler(e) {
		if(guestMode) return;
		if (e.target.classList.contains("whiteboard")) {
			e.preventDefault();

			clickDown = true;
			switch (currentTool) {
				case "text":
					let textElement = new TextElement();
					textElement.updatePosition(e.clientX - panX, e.clientY - panY);
					whiteboardElements.push(textElement);
					requestSave();
					break;
				case "pen":
					let penElement = new PenElement();
					penElement.updatePosition(e.clientX - panX, e.clientY - panY);
					penElement.addPointToPath(0, 0);
					penOriginalX = e.clientX - panX;
					penOriginalY = e.clientY - panY;
					currentlyModifyingElement = penElement;
					whiteboardElements.push(penElement);
					startEdit();
					break;
				case "erase":
					startEdit();
				default:
					break;
			}
		}
	}
	function pointerUpHandler(e) {
		if (clickDown) e.preventDefault();
		clickDown = false;
		if (
			currentlyModifyingElement != null &&
			currentlyModifyingElement.type == "pen"
		) {
			endEdit();
			requestSave();
		}
		currentlyModifyingElement = null;
	}
	function pointerMoveHandler(e) {
		if (clickDown) {
			e.preventDefault();
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
	function exportData() {
		let data = [];
		whiteboardElements.forEach((element) => {
			data.push(element.export());
		});
		return data;
	}
	let loadInterval = 0;
	$inspect(loggedIn)
	onMount(() => {
		if (
			getCookie("userId") === undefined ||
			getCookie("userId") === null ||
			getCookie("userId") === "-1"
		) {
			loggedIn = false;
		} else {
			loggedIn = true;
		}
		window.exportData = exportData;
		window.saveData = saveData;
		//load data
		let userId = getCookie("userId");
		loadData();
		loadInterval = setInterval(() => {
			if (currentlyEditingSomething || needToSave) return;
			console.log("loading");
			loadData();
		}, 200);
	});

	onDestroy(() => {
		clearInterval(loadInterval);
	});

	function loadData() {
		fetch(
			`${PUBLIC_LOCALHOST_URL}/whiteboard201/whiteboard/get?whiteboardId=${data.id}`
		)
			.then((response) => response.json())
			.then((data) => {
				importData(data.content);
			})
			.catch((error) => {
				console.error("Error fetching whiteboards:", error);
			});
	}

	function saveData() {
		let userId = getCookie("userId");
		console.log(exportData());
		fetch(
			`${PUBLIC_LOCALHOST_URL}/whiteboard201/whiteboard/save?id=${data.id}`,
			{
				method: "POST",
				body: JSON.stringify(exportData()),
				headers: {
					"Content-Type": "text/plain",
				},
			}
		)
			.then((response) => response.json())
			.then((data) => {
				console.log(exportData());
				console.log(data);
			})
			.catch((error) => {
				console.error("Error saving whiteboard:", error);
			});
	}
	let timestamp = $state(0);

	function importData(data) {
		whiteboardElements = [];
		timestamp = Date.now();
		if (!data) data = [];

		whiteboardElements = data.map((element) => {
			if (element.type === "text") {
				return new TextElement(element.content, element.properties);
			} else if (element.type === "pen") {
				// console.log(element);
				return new PenElement(element.content, element.properties);
			}
			return null;
		});
	}

	let needToSave = false;
	function requestSave() {
		if (needToSave) return;
		needToSave = true;
		setTimeout(() => {
			saveData();
			needToSave = false;
		}, 200);
	}
	let currentlyEditingSomething = false;
	function startEdit() {
		if (currentlyEditingSomething) return;
		currentlyEditingSomething = true;
	}
	function endEdit() {
		currentlyEditingSomething = false;
	}
	let eraseDebounce = 0;
	let elementsToErase = [];
	function requestEraseElement(element) {
		if (elementsToErase.includes(element)) return;
		elementsToErase.push(element);
		clearTimeout(eraseDebounce);
		eraseDebounce = setTimeout(() => {
			whiteboardElements = whiteboardElements.filter(
				(e) => !elementsToErase.includes(e)
			);
			elementsToErase = [];
			timestamp = Date.now();
			endEdit();
			requestSave();
		}, 1);
	}
</script>

<svelte:body
	on:pointerdown|nonpassive={pointerDownHandler}
	on:pointerup|nonpassive={pointerUpHandler}
	on:pointermove|nonpassive={pointerMoveHandler}
	on:wheel|nonpassive={wheelHandler}
/>

<div class="whiteboard" style:background-position={panX + "px " + panY + "px"}>
	{#key timestamp}
		{#each whiteboardElements as element, index}
			<div>
				{#if element.type === "text"}
					<TextElementComponent
					bind:guestMode={guestMode}
						{currentTool}
						{requestSave}
						{startEdit}
						{endEdit}
						{panX}
						{panY}
						{requestEraseElement}
						bind:elementData={whiteboardElements[index]}
					/>
				{:else if element.type == "pen"}
					<PenElementComponent
					bind:guestMode={guestMode}
						{currentTool}
						{requestEraseElement}
						{panX}
						{startEdit}
						{endEdit}
						{panY}
						bind:elementData={whiteboardElements[index]}
					/>
				{/if}
			</div>
		{/each}{/key}
</div>
{#if !guestMode}
<div class="toolbar">
	<button onclick={addTextElementHandler}> Add TextBox 💬</button>
	<button onclick={addBrushStrokeHandler}> Brush Stroke 🖌️</button>
	<button onclick={addEraserHandler}> Erase ⌫</button>
	<a href="/" class="home-btn"> Home 🏠</a>
</div>
{/if}
{#if !loggedIn && !guestMode}
	<div class="modal-wrapper">
		<div class="modal">
			<h1>Guest Mode</h1>
			<p>
				You aren't logged in! <br />You can either log in to edit this
				whiteboard, or continue as a guest to view it.
			</p>
			<a class="btn" href={"../login?redirect=" + data.id}>Log in</a>
			<button class="btn" onclick={()=>{guestMode = true;}}>Continue as Guest</button>
		</div>
	</div>
{/if}

{#if guestMode} 
<div class="guest-mode-wrapper">
	<div class="guest-mode-highlight">You are currently viewing this project in Guest mode. <a href={"../login?redirect=" + data.id}>Log in to edit.</a></div>
</div>
{/if}

<style>
	.modal-wrapper {
		position: fixed;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		background-color: rgba(0, 0, 0, 0.086);
		display: flex;
		align-items: center;
		justify-content: center;
		z-index: 1000;
		backdrop-filter: blur(5px);
	}
	.modal {
		background-color: white;
		padding: 20px;
		border-radius: 10px;
		box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
		text-align: center;
		width: 300px;
	}
	.btn {
		background-color: #f5a4dc;
		color: white;
		border: none;
		border-radius: 5px;
		padding: 10px 20px;
		font-size: 16px;
		cursor: pointer;
		margin-top: 10px;
		color: rgb(2, 2, 54);
		width: 100% !important;
		display: block;
		box-sizing: border-box;
		text-decoration: none;
	}
	button.btn {
		background: transparent;
	}
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

	.toolbar button,
	.toolbar .home-btn {
		padding: 6px 12px;
		font-size: 14px;
		cursor: pointer;
		background-color: #cfcfcf;
		color: white;
		border: none;
		border-radius: 5px;
		font-weight: bold;
		text-decoration: none; /* important for <a> */
		display: inline-flex;
		align-items: center;
		justify-content: center;
	}

	.toolbar button:hover,
	.toolbar .home-btn:hover {
		background-color: #f5a4dc;
	}
	.guest-mode-wrapper {
		position: fixed;
		top: 8px;
		right: 8px;
		bottom: 8px;
		left: 8px;
		outline: 20px solid #f5a4dc;
		border-radius: 10px;
	}
	.guest-mode-highlight {
		position: absolute;
		background-color: #f5a4dc;
		color: white;
		padding: 10px;
		font-size: 16px;
		text-align: center;
		width: fit-content;
		border: 8px;
		left: 50%;
		top: 10px;
		transform: translate(-50%);
		border-radius: 10px;
		color: black !important;
	}
	.guest-mode-highlight a {
		color: black !important;
		text-decoration: underline;
	}
</style>

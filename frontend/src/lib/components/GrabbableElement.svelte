<script>
	/**
	 * This component is not intended to be used directly in the application, and instead is used inside
	 * of other elements specifically to manage the grab mechanics. it is a wrapper of sorts.
	 */
	import { Element } from "$lib/elements";
	let {
		panX = 0,
		panY = 0,
		children,
		elementData = $bindable(null),
	} = $props();
	let pX = 0;
	let pY = 0;
	let elementX = $state(elementData.properties.x);
	let elementY = $state(elementData.properties.y);
	let width = $state(elementData.properties.width);
	let height = $state(elementData.properties.height);
	let translating = false;
	let translatingX = false;
	let translatingY = false;
	let resizing = false;
	let resizingXInvert = 1;
	let resizingYInvert = 1;
</script>

<svelte:body
	onpointerup={(e) => {
		if (!translating && !translatingX && !translatingY && !resizing) return;
		e.preventDefault();

		elementData.updatePosition(elementX, elementY);
		elementData.updateSize(width, height);
		translating = false;
		resizing = false;
		translatingX = false;
		translatingY = false;
	}}
	onpointermove={(e) => {
		if (translating) {
			e.preventDefault();
			elementX += translating ? e.clientX - pX : 0;
			elementY += translating ? e.clientY - pY : 0;
		}
		let fW = width + (e.clientX - pX) * resizingXInvert;
		let fH = height + (e.clientY - pY) * resizingYInvert;
		if (translatingX) {
			e.preventDefault();
			if(fW > 100) {
				elementX += e.clientX - pX;
			}
		}
		if (translatingY) {
			e.preventDefault();
			if(fH > 100) {
				elementY += e.clientY - pY;
			}
		}
		if (resizing) {
			e.preventDefault();
			if (fW > 100) {
				width = fW;
			}
			if (fH > 100) {
				height = fH;
			}
		}
		pX = e.clientX;
		pY = e.clientY;
	}}
/>

<div
	class="grabbable-wrapper"
	style:width={width + "px"}
	style:height={height + "px"}
	style:top={panY + elementY + "px"}
	style:left={panX + elementX + "px"}
>
	<div
		class="grabbable-top"
		onpointerdown={(e) => {
			e.preventDefault();
			pX = e.clientX;
			pY = e.clientY;
			translating = true;
		}}
	>
		<img
			src="data:image/svg+xml;utf8,<svg width='12' height='8' viewBox='0 0 12 8' xmlns='http://www.w3.org/2000/svg' fill='%23f5a4dc'><circle cx='1' cy='1' r='1'/><circle cx='6' cy='1' r='1'/><circle cx='11' cy='1' r='1'/><circle cx='1' cy='7' r='1'/><circle cx='6' cy='7' r='1'/><circle cx='11' cy='7' r='1'/></svg>"
			alt="grab icon"
		/>
	</div>
	<div
		class="grabbable-corner"
		onpointerdown={(e) => {
			e.preventDefault();
			pX = e.clientX;
			pY = e.clientY;
			resizing = true;
			resizingXInvert = -1;
			resizingYInvert = -1;
			translatingX = true;
			translatingY = true;
		}}
		style:top={"-12px"}
		style:left={"-12px"}
	></div>
	<div
		class="grabbable-corner corner-nesw"
		style:bottom={"-12px"}
		style:left={"-12px"}
		onpointerdown={(e) => {
			e.preventDefault();
			pX = e.clientX;
			pY = e.clientY;
			resizingXInvert = -1;
			resizingYInvert = 1;
			translatingX = true;
			resizing = true;
		}}
	></div>
	<div
		class="grabbable-corner corner-nesw"
		style:top={"-12px"}
		style:right={"-12px"}
		onpointerdown={(e) => {
			e.preventDefault();
			pX = e.clientX;
			pY = e.clientY;
			resizingYInvert = -1;
			resizingXInvert = 1;
			translatingY = true;
			resizing = true;
		}}
	></div>
	<div
		class="grabbable-corner"
		style:bottom={"-12px"}
		style:right={"-12px"}
		onpointerdown={(e) => {
			e.preventDefault();
			pX = e.clientX;
			pY = e.clientY;
			resizingXInvert = 1;
			resizingYInvert = 1;
			resizing = true;
		}}
	></div>
	{@render children()}
</div>

<style>
	.grabbable-wrapper {
		position: fixed;
		user-select: none;
		box-sizing: border-box;
		padding: 4px;
		min-width: 50px;
		min-height: 20px;
		border-radius: 0px;
	}
	.grabbable-corner {
		box-sizing: border-box;
		position: absolute;
		width: 14px;
		height: 14px;
		border-radius: 100%;
		cursor: nwse-resize;
		background: #fff;
		border: 2px solid #f5a4dc;
		opacity: 0;
	}
	.grabbable-top {
		box-sizing: border-box;
		position: absolute;
		width: 28px;
		height: 18px;
		border-radius: 50px;
		cursor: grab;
		background: #fff;
		border: 2px solid #f5a4dc;
		opacity: 0;
		left: 50%;
		transform: translateX(-50%);
		top: -14px;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	.grabbable-top:active {
		cursor: grabbing;
	}
	.grabbable-top img {
		height: 8px;
	}

	.corner-nesw {
		cursor: nesw-resize;
	}

	.grabbable-wrapper:hover {
		outline-offset: 4px;
		outline: 2px dashed #f5a4dc;
	}
	.grabbable-wrapper:hover .grabbable-corner,
	.grabbable-wrapper:hover .grabbable-top {
		opacity: 1;
	}
</style>

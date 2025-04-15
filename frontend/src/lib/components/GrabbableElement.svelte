<script>
	/**
	 * This component is not intended to be used directly in the application, and instead is used inside
	 * of other elements specifically to manage the grab mechanics. it is a wrapper of sorts.
	 */
	import { Element } from "$lib/elements";
	let {
		panX = 0,
		panY = 0,
		elementX = $bindable(0),
		elementY = $bindable(0),
		children,
		elementData = $bindable(null),
	} = $props();
	let pX = 0;
	let pY = 0;
	let translating = false;
</script>

<svelte:body
	onpointerup={(e) => {
		e.preventDefault();
		elementData.updatePosition(elementX, elementY);
		translating = false;
	}}
	onpointermove={(e) => {
		e.preventDefault();
		if (translating) {
			elementX += translating ? e.clientX - pX : 0;
			elementY += translating ? e.clientY - pY : 0;
		}
		pX = e.clientX;
		pY = e.clientY;
	}}
/>

<div
	class="grabbable-wrapper"
	style:width={elementData.properties.width + "px"}
	style:height={elementData.properties.height + "px"}
	style:top={panY + elementY + "px"}
	style:left={panX + elementX + "px"}
>
	<div class="grabbable-top"
    
	onpointerdown={(e) => {
		e.preventDefault();
		pX = e.clientX;
		pY = e.clientY;
		translating = true;
	}}>
		<img
			src="data:image/svg+xml;utf8,<svg width='12' height='8' viewBox='0 0 12 8' xmlns='http://www.w3.org/2000/svg' fill='%23666'><circle cx='1' cy='1' r='1'/><circle cx='6' cy='1' r='1'/><circle cx='11' cy='1' r='1'/><circle cx='1' cy='7' r='1'/><circle cx='6' cy='7' r='1'/><circle cx='11' cy='7' r='1'/></svg>"
			alt="grab icon"
		/>
	</div>
	<div class="grabbable-corner" style:top={"-12px"} style:left={"-12px"}></div>
	<div
		class="grabbable-corner corner-nesw"
		style:bottom={"-12px"}
		style:left={"-12px"}
	></div>
	<div
		class="grabbable-corner corner-nesw"
		style:top={"-12px"}
		style:right={"-12px"}
	></div>
	<div
		class="grabbable-corner"
		style:bottom={"-12px"}
		style:right={"-12px"}
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
		border-radius: 4px;
	}
	.grabbable-corner {
		box-sizing: border-box;
		position: absolute;
		width: 14px;
		height: 14px;
		border-radius: 100%;
		cursor: nwse-resize;
		background: #fff;
		border: 2px solid #a200ff;
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
		border: 2px solid #a200ff;
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
        height: 8px
    }

	.corner-nesw {
		cursor: nesw-resize;
	}

	.grabbable-wrapper:hover {
		background: #a200ff17;
		outline-offset: 4px;
		outline: 2px dashed #a200ff;
	}
	.grabbable-wrapper:hover .grabbable-corner, .grabbable-wrapper:hover .grabbable-top {
		opacity: 1;
	}

</style>

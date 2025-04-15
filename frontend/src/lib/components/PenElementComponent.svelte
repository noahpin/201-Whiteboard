<script>
	import GrabbableElement from "./GrabbableElement.svelte";
	let {
		panX = 0,
		panY = 0,
		elementX = $bindable(0),
		elementY = $bindable(0),
		elementData = $bindable(null),
	} = $props();
    $inspect(elementData);
</script>

<GrabbableElement {panX} {panY} bind:elementData>
</GrabbableElement>

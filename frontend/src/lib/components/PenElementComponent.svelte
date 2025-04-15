<script>
	import GrabbableElement from "./GrabbableElement.svelte";
    import { getStroke } from 'perfect-freehand'
	let {
		panX = 0,
		panY = 0,
		elementX = $bindable(0),
		elementY = $bindable(0),
		elementData = $bindable(null),
	} = $props();
    let derivedPath = $derived(elementData.path);
    elementData.onPathUpdated = onPathUpdated;
    let pathData = $state('')
    let svgPadding = 10;
    function onPathUpdated() {
        derivedPath = elementData.path;
        calculateBounds()
        let calculateAgainstPath = derivedPath.map(point => {
            return [point[0] - xOffset + svgPadding, point[1] - yOffset + svgPadding]
        })
        let stroke = getStroke(calculateAgainstPath, {
            size: 5,
            thinning: 0.5,
            smoothing: 0.5,
            streamline: 0.5,
            last: true
        })
        pathData = getSvgPathFromStroke(stroke)
    }
    function getSvgPathFromStroke(stroke) {
    if (!stroke.length) return ""

    const d = stroke.reduce(
        (acc, [x0, y0], i, arr) => {
        const [x1, y1] = arr[(i + 1) % arr.length]
        acc.push(x0, y0, (x0 + x1) / 2, (y0 + y1) / 2)
        return acc
        },
        ["M", ...stroke[0], "Q"]
    )

    d.push("Z")
    return d.join(" ")
    }
    let width = $state(0);
    let height = $state(0);
    let xOffset = $state(0);
    let yOffset = $state(0);

    function calculateBounds() {
        let minX = Math.min(...derivedPath.map(point => point[0]));
        let minY = Math.min(...derivedPath.map(point => point[1]));
        let maxX = Math.max(...derivedPath.map(point => point[0]));
        let maxY = Math.max(...derivedPath.map(point => point[1]));
        width = maxX - minX;
        height = maxY - minY;
        xOffset = minX;
        yOffset = minY;
    }

</script>
<!-- 
{#each derivedPath as point, index}
<div class="dummy" style:top={point[1] + panY-2 + "px"} style:left = {point[0]-2 + panX + "px"}></div>
{/each} -->
<svg style:touch-action="none" width={width + (svgPadding * 2)} height={height + (svgPadding * 2)} viewBox={`0 0 ${width + (svgPadding * 2)} ${height + (svgPadding * 2)}`} xmlns="http://www.w3.org/2000/svg" style:top={panY + elementY - svgPadding + yOffset + "px"} style:left={panX + elementX - svgPadding + xOffset + "px"}>
    <path d={pathData} fill="black" />
</svg>

<style>
    svg {
        position: absolute;
        pointer-events: none;
        background: #ff00ff33
    }
</style>
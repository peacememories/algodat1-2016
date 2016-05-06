var elements = document.querySelectorAll(".language-viz")

Array.prototype.forEach.call(elements, function(element) {
  var image = Viz(element.innerText, {format: "png-image-element"})
  element.parentElement.replaceChild(image, element)
})

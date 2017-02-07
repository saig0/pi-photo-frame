package de.pi.photo.frame

import scalafx.scene.control.TextField
import scalafx.scene.control.Button
import scalafx.scene.control.ListView
import scalafx.event.ActionEvent
import scalafxml.core.macros.sfxml
import scalafx.scene.image.ImageView
import javafx.scene.image.Image

/**
 * @author Philipp
 */
@sfxml
class Controller(
	private val imageView: ImageView) {
  
	imageView.image.setValue( new Image("photo_1.JPG") )
	
}
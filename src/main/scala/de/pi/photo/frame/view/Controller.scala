package de.pi.photo.frame.view

import scalafxml.core.macros.sfxml
import scalafx.scene.image.ImageView
import javafx.scene.image.Image
import scalafx.beans.property.ObjectProperty.sfxObjectProperty2jfx
import de.pi.photo.frame.model.Configuration
import scalafx.scene.control.Label
import java.util.Timer
import java.util.TimerTask

/**
 * @author Philipp
 */
@sfxml
class Controller(
	private val imageView: ImageView,
	private val noImagesFoundLabel: Label) {
  
	var currentImage = 0
	
	// TODO terminate thread on exit
	
	Configuration.getImagePaths match {
		case Nil => noImagesFoundLabel.visible = true
		case images => new Timer().schedule(new TimerTask 
		{
			def run = {
				val img = images(currentImage % images.size)
				
				imageView.image.value = new Image(img)	
				
				currentImage += 1
			}		
			
		}, 0, 5000)
	}
	
}
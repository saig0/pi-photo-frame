package de.pi.photo.frame.view

import scalafxml.core.macros.sfxml
import scalafx.scene.image.ImageView
import javafx.scene.image.Image
import scalafx.beans.property.ObjectProperty.sfxObjectProperty2jfx
import de.pi.photo.frame.model.Configuration
import scalafx.scene.control.Label
import java.util.Timer
import java.util.TimerTask
import scalafx.animation.Timeline
import scalafx.Includes._
import scalafx.animation.Interpolator
import scalafx.stage.Stage

/**
 * @author Philipp
 */
@sfxml
class Controller(
	private val imageView1: ImageView,
	private val imageView2: ImageView,
	private val noImagesFoundLabel: Label) {
  
	var currentImage = 0
	
	val timeline = new Timeline {
    cycleCount = Timeline.Indefinite
    autoReverse = false
    keyFrames = Seq(
      at (0 s) {imageView1.opacity -> 1},
      at (0 s) {imageView2.opacity -> 0},
      at (8 s) {imageView1.opacity -> 1},
      at (8 s) {imageView2.opacity -> 0},
      at (9 s) {imageView1.opacity -> 0.5},
      at (9 s) {imageView2.opacity -> 0.5},
      at (10 s) {imageView1.opacity -> 0},
      at (10 s) {imageView2.opacity -> 1},
      at (18 s) {imageView1.opacity -> 0},
      at (18 s) {imageView2.opacity -> 1},
      at (19 s) {imageView1.opacity -> 0.5},
      at (19 s) {imageView2.opacity -> 0.5},
      at (20 s) {imageView1.opacity -> 1},
      at (20 s) {imageView2.opacity -> 0}
    )
	}
	
	// TODO terminate thread on exit
	
	Configuration.getImagePaths match {
		case Nil => noImagesFoundLabel.visible = true
		case images => new Timer().schedule(new TimerTask 
		{
			def run = {
				val img = images(currentImage % images.size)
					
				if (currentImage % 2 == 0)
				{
					imageView1.image.value = new Image(img)
				}
				else
				{
					imageView2.image.value = new Image(img)
				}
								
				currentImage += 1
			}		
						
		}, 5000, 10000)
		
		// load first image
		val img = images(currentImage % images.size)
		imageView1.image.value = new Image(img)
		
		currentImage += 1
		
		timeline.play
	}
	
}
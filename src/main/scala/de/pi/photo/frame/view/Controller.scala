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
import java.nio.file.Paths
import scalafx.event.ActionEvent
import scalafx.scene.layout.VBox
import scalafx.stage.FileChooser
import scalafx.stage.FileChooser.ExtensionFilter
import scalafx.stage.DirectoryChooser
import scalafx.scene.input.MouseEvent

/**
 * @author Philipp
 */
@sfxml
class Controller(
	private val imageView1: ImageView,
	private val imageView2: ImageView,
	private val noImagesFoundLabel: Label,
	private val menu: VBox) {

	var images: List[String] = List()
	var currentImage = 0
	
	private def getNextImage: Image = {
		val img = images(currentImage % images.size)
  	currentImage += 1		
				
		new Image(img)
	}
		
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
	
	Configuration.getImagePaths match {
		case Nil => noImagesFoundLabel.visible = true
		case urls => {
			images = urls 
			
			// TODO terminate thread on exit
			new Timer().schedule(new TimerTask 
			{
				def run = {
					
					if (currentImage % 2 == 0)
					{
						imageView1.image = getNextImage
					}
					else
					{
						imageView2.image = getNextImage
					}
				}							
			}, 5000, 10000)
		
			// load first image
			imageView1.image = getNextImage
		
			timeline.play
		}
	}
	
	def onShowMenu(event: MouseEvent) {
		menu.visible = true	
	}
	
	def onMenuClose(event: MouseEvent) {
		menu.visible = false
	}
	
	def onSelectPhotoDirectory(event: MouseEvent) {
	
		val dirChooser = new DirectoryChooser()
		dirChooser.title = "Select photo directory"
		
		//fileChooser.setInitialDirectory(viewModel.getDefaultLogFileLocation())

		//fileChooser.showOpenDialog(primaryStage)
		
	}
	
}
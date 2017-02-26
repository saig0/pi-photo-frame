package de.pi.photo.frame.view

import scalafxml.core.macros.sfxml
import scalafx.scene.image.ImageView
import scalafx.scene.image.Image
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
import scalafx.stage.WindowEvent
import scalafx.application.Platform
import de.pi.photo.frame.model.Configuration
import java.io.File
import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.exif.ExifDirectoryBase.TAG_ORIENTATION
import com.drew.metadata.exif.ExifIFD0Directory
import java.nio.file.Path
import scalafx.scene.layout.StackPane
import scalafx.scene.control.ProgressBar

@sfxml
class Controller(
	private val imageView1: ImageView,
	private val imageView2: ImageView,
	private val noImagesFoundLabel: Label,
	private val menu: VBox,
	private val progressPane: StackPane,
	private val imageViewPane1: StackPane,
	private val imageViewPane2: StackPane) {
	
	var config: Configuration = _
	
	var images: List[Path] = List()
	var currentImage = 0
		
	def onShowMenu(event: MouseEvent) {
		menu.visible = true	
	}
	
	def onMenuClose(event: MouseEvent) {
		menu.visible = false
	}
	
	def onSelectPhotoDirectory(event: MouseEvent) {
	
		val dirChooser = new DirectoryChooser()
		dirChooser.title = "Select photo directory"
		
		val dir = new File(config.directory)
		
		if (dir.exists)
		{
			dirChooser.initialDirectory = dir			
		}		

		val selectedDir = dirChooser.showDialog(Main.stage)		
		
		if (selectedDir != null)
		{
			config.directory = selectedDir.getAbsolutePath	
			config.save
			
			loadImages
		}
				
		menu.visible = false
	}
	
	def loadImages() {
		
		progressPane.visible = true
		
		Platform.runLater {
			
			config.loadImages match {
				case Nil => {
					noImagesFoundLabel.visible = true
					imageView1.visible = false
					imageView2.visible = false			
				}
				case urls => {
					noImagesFoundLabel.visible = false
					imageView1.visible = true
					imageView2.visible = true
					
					images = urls 
				
					reset
				}
			}
			
			progressPane.visible = false			
		}
	}	
	
	private def reset {
		
		// TODO ensure that the current cycle ends
		if (imageSwitchTask != null)
		{
			imageSwitchTask.cancel
		}
		
		imageView2.image = null
		
		imageView2.opacity = 1
		imageView1.opacity = 0
		
		currentImage = 0
		
		showNextImage(imageView1)
	}
	
	private def showNextImage(view: ImageView) {
		val path = images(currentImage % images.size)
  	currentImage += 1		
		
		val img = new Image(path.toUri.toURL.toExternalForm)
		view.image = img

		rotateImage(view, path)
		
		// schedule next image
		
		if (currentImage % 2 == 0)
		{
			scheduleNextImage(imageView1)
			
			timelineToView2.play				
		}
		else 
		{
			scheduleNextImage(imageView2)
			
			timelineToView1.play
		}
	}
	
	private def rotateImage(view: ImageView, path: Path) {
		val metadata = ImageMetadataReader.readMetadata(path.toFile)
		val directory = metadata.getFirstDirectoryOfType(classOf[ExifIFD0Directory])	
		val orientation = directory.getInt(TAG_ORIENTATION)		
		
		val width = view.fitWidth.get
		val height = view.fitHeight.get
		
		orientation match {
			case 1 => {
				view.rotate = 0
			
				if (height > width)
				{
					view.fitWidth = height
 					view.fitHeight = width
				}
			}				
			case 3 => {
				view.rotate = 180
			
				if (height > width)
				{
					view.fitWidth = height
 					view.fitHeight = width
				}
			}			
			case 6 => {
				view.rotate = 90
				
				if (width > height)
				{
					view.fitWidth = height
 					view.fitHeight = width
				}
		 	}
			case 8 => {
				view.rotate = 270
				
				if (width > height)
				{
					view.fitWidth = height
 					view.fitHeight = width
				}
		 	}
		}
	}
		
	val timelineToView2 = new Timeline {
    
    keyFrames = Seq(
      at (0 s) {imageView1.opacity -> 1},
      at (0 s) {imageView2.opacity -> 0},
      at (1 s) {imageView1.opacity -> 0.5},
      at (1 s) {imageView2.opacity -> 0.5},
      at (2 s) {imageView1.opacity -> 0},
      at (2 s) {imageView2.opacity -> 1}
    )
	}
	
	val timelineToView1 = new Timeline {
    
    keyFrames = Seq(
      at (0 s) {imageView1.opacity -> 0},
      at (0 s) {imageView2.opacity -> 1},
      at (1 s) {imageView1.opacity -> 0.5},
      at (1 s) {imageView2.opacity -> 0.5},
      at (2 s) {imageView1.opacity -> 1},
      at (2 s) {imageView2.opacity -> 0}
    )
	}
	
	private def scheduleNextImage(view: ImageView) {
		imageSwitchTask = new ImageViewSwitchTask(view)
		imageSwitchTimer.schedule(imageSwitchTask, 10000)
	}
	
  val imageSwitchTimer = new Timer()
  var imageSwitchTask: ImageViewSwitchTask = _
		
  class ImageViewSwitchTask(view: ImageView) extends TimerTask 
  {
  	def run {
  		showNextImage(view)
  	}
  }
	
	Platform.runLater {
		
		// maximize image views
		imageView1.fitWidth <= Main.stage.width
		imageView1.fitHeight <= Main.stage.height
		imageView2.fitWidth <= Main.stage.width
		imageView2.fitHeight <= Main.stage.height
		
		imageViewPane1.prefWidth <= Main.stage.width
		imageViewPane1.prefHeight <= Main.stage.height
		
		imageViewPane2.prefWidth <= Main.stage.width
		imageViewPane2.prefHeight <= Main.stage.height
		
		// load images
		config = Configuration.load
		
		loadImages()
				
		Main.stage.onCloseRequest = (event: WindowEvent) => {
			imageSwitchTimer.cancel
		}
		
	}
	
}
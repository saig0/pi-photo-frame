package de.pi.photo.frame

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.paint.Color._
import scalafx.scene.shape.Rectangle
import javafx.fxml.FXMLLoader
import javafx.scene.Parent


/**
 * @author Philipp
 */
object Main extends JFXApp {
  
	val fxmlLoader = new FXMLLoader(getClass().getResource("/main.fxml"))
	
	val mainView: Parent = fxmlLoader.load()
	
	stage = new JFXApp.PrimaryStage {
    title.value = "Pi Photo Frame"
    width = 600
    height = 450
    
    scene = new Scene {
    	root = mainView
    }
    
    centerOnScreen
  }
	
}
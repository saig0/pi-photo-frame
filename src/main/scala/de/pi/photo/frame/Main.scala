package de.pi.photo.frame

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.paint.Color._
import scalafx.scene.shape.Rectangle
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import scalafxml.core.DependenciesByType
import scalafxml.core.FXMLView


/**
 * @author Philipp
 */
object Main extends JFXApp {
	
	stage = new JFXApp.PrimaryStage {
    title.value = "Pi Photo Frame"
    width = 600
    height = 450
    
    scene = new Scene(
        FXMLView(
            getClass.getResource("/main.fxml"),
            new DependenciesByType(Map()))
    )
    
    centerOnScreen
  }
	
}
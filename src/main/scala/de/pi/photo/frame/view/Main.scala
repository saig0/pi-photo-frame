package de.pi.photo.frame.view

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.paint.Color._
import scalafxml.core.DependenciesByType
import scalafxml.core.FXMLView
import scalafx.application.Platform


/**
 * @author Philipp
 */
object Main extends JFXApp {
	
	stage = new JFXApp.PrimaryStage {
    title.value = "Pi Photo Frame"
    
    scene = new Scene(
        FXMLView(
            getClass.getResource("/main.fxml"),
            new DependenciesByType(Map()))
    )
    
    width = 1280
    height = 1024
    
    maximized = true    
		fullScreen = true   
  }
	
}
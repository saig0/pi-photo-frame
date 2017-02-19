package de.pi.photo.frame.model

import java.nio.file.Files
import java.nio.file.Paths
import java.io.File
import java.io.FilenameFilter
import java.nio.file.Path
import java.net.URL

/**
 * @author Philipp
 */
object Configuration {
  
	// TODO make directory configurable
	var directory = Paths.get("photos")

	// TODO support more images type
	val fileFilter: FilenameFilter = new FilenameFilter {
		def accept(file: File, name: String) = name.toLowerCase().endsWith(".jpg")
	}
	
	def loadImages: List[String] = 
		if (Files.exists(directory)){
			directory.toFile.listFiles( fileFilter ).map(_.toURL().toExternalForm()).toList
		} else {
			List()
		}
	
}
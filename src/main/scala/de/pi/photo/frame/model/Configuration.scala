package de.pi.photo.frame.model

import java.nio.file.Files
import java.nio.file.Paths
import java.io.File
import java.io.FilenameFilter
import java.nio.file.Path
import java.net.URL
import java.io.FileOutputStream
import java.io.FileInputStream
import java.nio.charset.StandardCharsets


/**
 * @author Philipp
 */
class Configuration(var directory: String) {
  
	// TODO support more images type
	val fileFilter: FilenameFilter = new FilenameFilter {
		def accept(file: File, name: String) = name.toLowerCase().endsWith(".jpg")
	}
	
	def loadImages: List[String] = {
		
		val dir = if (!directory.isEmpty) new File(directory) else Configuration.defaultPhotoDir 
		
		if (dir.exists){
			// TODO look recursivly
			dir.listFiles( fileFilter ).map(_.toURL().toExternalForm()).toList
		} else {
			List()
		}
	}
	
	def save = Configuration.save(this)
	
}

object Configuration {

	import net.liftweb.json._
	import net.liftweb.json.Serialization._
	
	private implicit val formats = DefaultFormats
	
	lazy val configLocation: File = new File(userDirectory, "pi-photo-frame.conf")
	
	lazy val userDirectory: File = new File(System.getProperty("user.home"))
	
	lazy val defaultPhotoDir = new File(userDirectory, "photo")
	
	def load: Configuration = {		
		if (configLocation.exists)
		{
			val bytes = Files.readAllBytes(configLocation.toPath)
			val json = new String(bytes, StandardCharsets.UTF_8)
			
			parse(json).extract[Configuration]			
		}
		else 
		{
			new Configuration(defaultPhotoDir.getAbsolutePath)	
		}		
	}

	def save(config: Configuration) {
		
		val json = write(config)
		val bytes = json.getBytes(StandardCharsets.UTF_8)
		
		try 
		{
			Files.write(configLocation.toPath, bytes)
		}
		catch {
			case t: Throwable => {
				System.err.println("Failed to save configuration")	
				t.printStackTrace()
			}
		}
	}
  
}
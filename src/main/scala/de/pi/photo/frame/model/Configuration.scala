package de.pi.photo.frame.model

import scala.collection.JavaConversions._
import java.nio.file.Files
import java.nio.file.Paths
import java.io.File
import java.io.FilenameFilter
import java.nio.file.Path
import java.net.URL
import java.io.FileOutputStream
import java.io.FileInputStream
import java.nio.charset.StandardCharsets
import scala.util.Random
import java.nio.file.FileVisitor
import java.util.stream.Collectors
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.FileVisitResult
import java.io.IOException
import java.io.IOException

/**
 * @author Philipp
 */
class Configuration(var directory: String) {
  	
	implicit def toJavaPredicate[A](f: Function1[A, Boolean]) = new java.util.function.Predicate[A] {
			override def test(a: A): Boolean = f(a)
	}
	
	implicit def toJavaFunction[A, B](f: Function1[A, B]) = new java.util.function.Function[A, B] {
	  override def apply(a: A): B = f(a)
	}
		
	private def isImage = (path: Path) => {
 		val p = path.toString.toLowerCase

		(			 p.endsWith(".jpg") 
				|| p.endsWith(".jpeg") 
				|| p.endsWith(".png") 
				|| p.endsWith(".gif") 
				|| p.endsWith(".bmp"))
	}

	def loadImages: List[Path] = {
		
		val dir = if (!directory.isEmpty) new File(directory) else Configuration.defaultPhotoDir 
		
		if (dir.exists)
		{
			val imageBufferList = List[Path]().toBuffer
			
			Files.walkFileTree(dir.toPath, new FileVisitor[Path] {
				
				def preVisitDirectory(directory: Path, attr: BasicFileAttributes) = FileVisitResult.CONTINUE
				
				def postVisitDirectory(directory: Path, e: IOException) = FileVisitResult.CONTINUE
				
				def visitFile(file: Path, attr: BasicFileAttributes) = {
					
					if (isImage(file)) {
						imageBufferList += file		
					}
					
					FileVisitResult.CONTINUE
				}
				
				def visitFileFailed(file: Path, e: IOException) = FileVisitResult.CONTINUE
				
			})
						
			Random.shuffle(imageBufferList.toList)
			
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
	
	private def defaultConfig = new Configuration(defaultPhotoDir.getAbsolutePath)
	
	def load: Configuration = {		
		
		try 
		{		
			if (configLocation.exists) 
			{		
					val bytes = Files.readAllBytes(configLocation.toPath)
					val json = new String(bytes, StandardCharsets.UTF_8)
					
					parse(json).extract[Configuration]							
			}
			else 
			{
				defaultConfig
			}		
		
		} catch {
				case t: Throwable => {
					System.err.println("Failed to load configuration: " + configLocation)
					t.printStackTrace
					
					defaultConfig	
				}
		}		
	}

	def save(config: Configuration) {
		
		val json = write(config)
		val bytes = json.getBytes(StandardCharsets.UTF_8)
		
		try 
		{
			Files.write(configLocation.toPath, bytes)
		
		} catch {
			case t: Throwable => {
				System.err.println("Failed to save configuration")	
				t.printStackTrace
			}
		}
	}
  
}
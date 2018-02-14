package cn.zry.danneydiary.utils

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


/**
 * Created by zry on 2018/2/14.
 */
object FileUtils {

    /***
     * 复制文件或目录
     */
    fun copy(fromFile: String, toFile: String): Int {
        val origFile = File(fromFile)
        val destFile = File(toFile)
        if (!origFile.exists()) {
            return -1
        }
        var fileCount = 0
        if (origFile.isDirectory) {
            val files = origFile.listFiles()
            if (!destFile.exists())
                destFile.mkdirs()
            //遍历要复制该目录下的全部文件
            for (file in files) {
                if (file.isDirectory)
                    fileCount += copy(file.path + "/", toFile + file.name + "/")
                else
                    fileCount += copy(file.path, toFile + file.name)
            }
        } else {  //如果复制文件
            val fis = FileInputStream(fromFile)
            val fos = FileOutputStream(toFile)
            val buffer = ByteArray(1024)
            var lenth = 0
            while (fis.read(buffer) > 0) {
                fos.write(buffer)
            }
            fis.close()
            fos.close()
            fileCount++
        }
        return fileCount
    }

}
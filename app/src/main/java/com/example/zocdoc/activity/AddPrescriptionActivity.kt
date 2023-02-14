package com.example.zocdoc.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.withStarted
import com.example.zocdoc.databinding.ActivityAddPrescriptionBinding
import com.example.zocdoc.progressbar.ProgressBar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class AddPrescriptionActivity : AppCompatActivity() {
    private lateinit var dataBinding : ActivityAddPrescriptionBinding
    private lateinit var sharedPreferences: SharedPreferences
    var dbref : DatabaseReference ?= null
    lateinit var fileUri : Uri
    private var select = false
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityAddPrescriptionBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)

        progressBar = ProgressBar(this)

        sharedPreferences = baseContext.getSharedPreferences("UserDate", Context.MODE_PRIVATE)

        dataBinding.cancelfile.setOnClickListener{
            dataBinding.filetitle.text.clear()
            select = false
            dataBinding.cancelfile.visibility = View.GONE
            dataBinding.filelogo.visibility = View.INVISIBLE
            dataBinding.cancelfile.visibility = View.VISIBLE
            dataBinding.imagebrowse.visibility = View.VISIBLE
        }

        dbref = FirebaseDatabase.getInstance().getReference("Users")

        // Browse PDF from the file manager
        dataBinding.imagebrowse.setOnClickListener(View.OnClickListener {
            Dexter.withContext(applicationContext)
                    .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(object : PermissionListener{
                        override fun onPermissionGranted(permissionGrand: PermissionGrantedResponse?) {
                            val intent = Intent()
                            intent.type = "application/pdf"
                            intent.action = Intent.ACTION_GET_CONTENT
                            startActivityForResult(
                                Intent.createChooser(intent, "Select pdf file"),
                                101
                            )
                        }

                    override fun onPermissionDenied(permissionDenied: PermissionDeniedResponse?) {
                        Toast.makeText(baseContext, "Permission denied", Toast.LENGTH_SHORT).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(permissionRequest: PermissionRequest?, permissionToken: PermissionToken?) {
                        permissionToken?.continuePermissionRequest()
                    }
                }).check()
        })

        // upload pdf
        dataBinding.imageupload.setOnClickListener{
            if (!select){
                Toast.makeText(this, "Please select the prescription", Toast.LENGTH_SHORT).show()
            }
            else{
                processUpload(fileUri)
            }
        }
    }

    @SuppressLint("Recycle")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK){
            assert(data != null)
            fileUri = data?.data!!

            // Show pdf in image view
            val fileDescriptor = contentResolver.openFileDescriptor(fileUri, "r")
            if (fileDescriptor != null) {
                val pdfRenderer = PdfRenderer(fileDescriptor)
                if (pdfRenderer.pageCount > 0) {
                    val page = pdfRenderer.openPage(0)
                    val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    dataBinding.filelogo.setImageBitmap(bitmap)
                    page.close()
                }
                pdfRenderer.close()
                fileDescriptor.close()
            }

            select = true
            dataBinding.filelogo.visibility = View.VISIBLE
            dataBinding.cancelfile.visibility = View.VISIBLE
            dataBinding.imagebrowse.visibility = View.VISIBLE
        }
    }

    private fun processUpload(fileUri: Uri) {
        val userId = sharedPreferences.getString("uid", "").toString()

        if (dataBinding.filetitle.text.isEmpty()){
            Toast.makeText(this, "Add file title", Toast.LENGTH_SHORT).show()
        }else{
            //progressBar.startDialog()
            val pro = ProgressDialog(this)
            pro.setTitle("Uploading...")
            pro.setMessage("Your file ${dataBinding.filetitle.text} uploading..")
            pro.setCancelable(false)
            pro.show()

            val reference : StorageReference = FirebaseStorage.getInstance().reference.child("uploads/"+ dataBinding.filetitle.text.toString().trim() + System.currentTimeMillis()+".pdf")
            reference.putFile(fileUri).addOnSuccessListener{
                reference.downloadUrl.addOnSuccessListener{
                    uri: Uri ->
                        val editor = sharedPreferences.edit()
                        editor.putString("prescription", uri.toString())
                        editor.apply()
                        //dbref?.child("Users")?.child(userId)?.child("prescription")?.setValue(uri.toString())
                        FirebaseDatabase.getInstance().getReference("Users").child(userId).child("prescription").setValue(uri.toString())

                        pro.dismiss()

                        dataBinding.filetitle.setText("")
                        dataBinding.filelogo.visibility = View.INVISIBLE
                        dataBinding.cancelfile.visibility = View.INVISIBLE
                        dataBinding.imagebrowse.visibility = View.VISIBLE

                    }
            }.addOnProgressListener { upload : UploadTask.TaskSnapshot ->
                val percentage = (100.0 * upload.bytesTransferred) / upload.totalByteCount
                pro.setMessage("Uploaded : ${percentage.toInt()}%")
            }.addOnCompleteListener{
                if (it.isSuccessful){
                    dataBinding.uploaded.visibility = View.VISIBLE
                    select = false
                    Toast.makeText(this, "Uploaded!", Toast.LENGTH_LONG).show()
                    pro.dismiss()
                }else{
                    Toast.makeText(this, "Upload failed!", Toast.LENGTH_SHORT).show()
                    pro.dismiss()
                }
            }

        }

    }
}
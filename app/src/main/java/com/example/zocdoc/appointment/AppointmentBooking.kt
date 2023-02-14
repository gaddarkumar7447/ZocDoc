package com.example.zocdoc.appointment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import com.example.zocdoc.R
import com.example.zocdoc.activity.BookingDoneActivity
import com.example.zocdoc.databinding.ActivityAppointmentBookingBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.FirebaseDatabase
import com.ncorti.slidetoact.SlideToActView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class AppointmentBooking : AppCompatActivity() {
    private lateinit var dataBinding : ActivityAppointmentBookingBinding
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var mapOfDiseasesList : HashMap<String, ArrayList<String>>
    private lateinit var cardiologistList : ArrayList<String>
    private lateinit var dentistList : ArrayList<String>
    private lateinit var entList : ArrayList<String>
    private lateinit var gynaecologistList : ArrayList<String>
    private lateinit var orthopaedicList : ArrayList<String>
    private lateinit var psychiatristsList : ArrayList<String>
    private lateinit var radiologistList : ArrayList<String>
    private lateinit var pulmonologistList : ArrayList<String>
    private lateinit var neurologistList : ArrayList<String>
    private lateinit var allergistsList : ArrayList<String>
    private lateinit var gastroenterologistsList : ArrayList<String>
    private lateinit var urologistsList : ArrayList<String>
    private lateinit var otolaryngologistsList : ArrayList<String>
    private lateinit var rheumatologistsList : ArrayList<String>
    private lateinit var anesthesiologistsList : ArrayList<String>


    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityAppointmentBookingBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)

        sharedPreferences = this.getSharedPreferences("UserData", Context.MODE_PRIVATE)

        initializeSpecializationWithDiseasesLists()

        val diseasesValue = HashMap<String, Float>()
        setDiseasesValue(diseasesValue)

        val conditionValue = HashMap<String, Float>()
        setConditionValue(conditionValue)


        var  totalPoint = 0
        val doctorUId = intent.extras?.getString("Duid")
        val doctorName = intent.extras?.getString("Dname")
        val doctorEmail = intent.extras?.getString("Demail")
        val doctorPhone = intent.extras?.getString("Dphone")
        val doctorType = intent.extras?.getString("Dtype")

        dataBinding.selectDate.setOnClickListener{
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "datePicker")
            datePicker.addOnPositiveButtonClickListener {
                val dateFormate = SimpleDateFormat("dd-MM-yyyy")
                val date = dateFormate.format(Date(it))
                dataBinding.selectDate.setText(date)
            }

            datePicker.addOnCancelListener {  }
            datePicker.addOnNegativeButtonClickListener{}
        }

        // Booking appointment
        dataBinding.btnFinalbook.onSlideCompleteListener = object  : SlideToActView.OnSlideCompleteListener{
            override fun onSlideComplete(view: SlideToActView) {
                val userName = sharedPreferences.getString("name", "").toString()
                val userPhone = sharedPreferences.getString("phone", "").toString()
                val userUid = sharedPreferences.getString("uid", "").toString()
                val userPrescription = sharedPreferences.getString("prescription", "").toString()

                val date = dataBinding.selectDate.text.toString()
                val time = dataBinding.timeDropdown.text.toString()
                val diseases = dataBinding.diseaseDropdown.text.toString()
                val situation = dataBinding.situationDropdown.text.toString()

                val rightNow = Calendar.getInstance()
                val calenderIn24Formate : Int = rightNow.get(Calendar.HOUR_OF_DAY)
                val firstComeFirstServe = 1 + (0.1 * ((calenderIn24Formate / 10) + 1))

                var temp = diseasesValue[diseases]!!
                temp += conditionValue[situation]!!

                totalPoint = (temp * firstComeFirstServe).toInt()

                // Empty hash Map for the Patient
                val appointmentD : HashMap<String, String> = HashMap()
                appointmentD["Patient Name"] = userName
                appointmentD["PatientPhone"] = userPhone
                appointmentD["Time"] = time
                appointmentD["Date"] = date
                appointmentD["Diseases"] = diseases
                appointmentD["PatientCondition"] = situation
                appointmentD["Prescription"] = userPrescription
                appointmentD["TotalPoints"] = totalPoint.toString().trim()

                val appointmentP : HashMap<String, String> = HashMap() //define empty hashmap
                appointmentP["DoctorUID"] = doctorUId.toString()
                appointmentP["DoctorName"] = doctorName.toString()
                appointmentP["DoctorPhone"] = doctorPhone.toString()
                appointmentP["DoctorEmail"] = doctorEmail.toString()
                appointmentP["Date"] = date
                appointmentP["Time"] = time
                appointmentP["Disease"] = diseases
                appointmentP["PatientCondition"] = situation
                appointmentP["Prescription"] = userPrescription

                val appointmentDB_Doctor = FirebaseDatabase.getInstance().getReference("Doctor").child(doctorUId!!).child("DoctorsAppointments").child(date)
                appointmentDB_Doctor.child(userUid).setValue(appointmentD)

                val appointmentDB_User_Doctor = FirebaseDatabase.getInstance().getReference("Users").child(doctorUId).child("DoctorsAppointments").child(date)
                appointmentDB_User_Doctor.child(doctorUId).setValue(appointmentP)

                val appointmentDB_Patient = FirebaseDatabase.getInstance().getReference("Users").child(userUid).child("PatientsAppointments").child(date)
                appointmentDB_Patient.child(doctorUId).setValue(appointmentP)


                // vibrate
                view.bumpVibration = 100
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (vibrator.hasVibrator()) {
                    vibrator.vibrate(100)
                }
                dataBinding.btnFinalbook.resetSlider()

                startActivity(Intent(this@AppointmentBooking, BookingDoneActivity::class.java))
                finish()
            }
        }

        // diseases list
        val item : List<String> = mapOfDiseasesList[doctorType]!!
        val adapter = ArrayAdapter(this, R.layout.item_text, item)
        dataBinding.diseaseDropdown.setAdapter(adapter)

        // situation list
        val situationList = listOf<String>("Severe Pain", "Mild Pain", "No Pain")
        val adapterS = ArrayAdapter(this, R.layout.item_text, situationList)
        dataBinding.situationDropdown.setAdapter(adapterS)

        // item time
        val itemTime = listOf<String>("9:00 AM - 11:00 AM","11:00 AM - 13:00 PM", "17:00 PM - 19:00 PM","19:00 PM - 22:OO PM")
        val adapterT = ArrayAdapter(this, R.layout.item_text, itemTime)
        dataBinding.timeDropdown.setAdapter(adapterT)

    }

    private fun initializeSpecializationWithDiseasesLists() {
        mapOfDiseasesList = HashMap()
        cardiologistList = ArrayList()
        dentistList = ArrayList()
        entList = ArrayList()
        gynaecologistList = ArrayList()
        orthopaedicList = ArrayList()
        psychiatristsList = ArrayList()
        radiologistList = ArrayList()
        pulmonologistList = ArrayList()
        neurologistList = ArrayList()
        allergistsList = ArrayList()
        gastroenterologistsList = ArrayList()
        urologistsList = ArrayList()
        otolaryngologistsList = ArrayList()
        rheumatologistsList = ArrayList()
        anesthesiologistsList = ArrayList()

        //List initializing for Cardiac diseases
        cardiologistList.add("Not sure")
        cardiologistList.add("High blood pressure")
        cardiologistList.add("High cholesterol")
        cardiologistList.add("Angina (chest pain)")
        cardiologistList.add("Heart rhythm disorders")
        cardiologistList.add("Atrial fibrillation")

        //List initializing for Dental diseases
        dentistList.add("Not sure")
        dentistList.add("Tooth Decay/Cavities")
        dentistList.add("Gum Disease")
        dentistList.add("Cracked or Broken Teeth")
        dentistList.add("Root Infection")
        dentistList.add("Tooth Loss")

        //List initializing for ENT diseases
        entList.add("Not sure")
        entList.add("Hearing problems")
        entList.add("Allergies")
        entList.add("Nasal congestion")
        entList.add("Tonsil infections")
        entList.add("Enlarged tonsils")

        //List initializing for ENT diseases
        gynaecologistList.add("Not sure")
        gynaecologistList.add("Bleeding during pregnancy")
        gynaecologistList.add("Female infertility")
        gynaecologistList.add("Heart disease in pregnancy")
        gynaecologistList.add("Menopause")
        gynaecologistList.add("Menstrual cramps")
        gynaecologistList.add("Miscarriage")
        gynaecologistList.add("Ovarian cysts")
        gynaecologistList.add("Vaginal bleeding")

        //List initializing for Orthopaedic diseases
        orthopaedicList.add("Not sure")
        orthopaedicList.add("Bone fractures")
        orthopaedicList.add("Muscle strains")
        orthopaedicList.add("Joint or back pain")
        orthopaedicList.add("Injuries to tendons or ligaments")
        orthopaedicList.add("Limb abnormalities")
        orthopaedicList.add("Bone cancer")

        //List initializing for Psychiatrists diseases
        psychiatristsList.add("Not sure")
        psychiatristsList.add("Alcohol use disorder")
        psychiatristsList.add("Alzheimer’s disease")
        psychiatristsList.add("Anxiety disorders")
        psychiatristsList.add("Bipolar disorder")
        psychiatristsList.add("Depression")
        psychiatristsList.add("Eating disorders")
        psychiatristsList.add("Mood disorders")
        psychiatristsList.add("Panic disorder")
        psychiatristsList.add("Sleep disorders")

        //List initializing for Radiologists treated diseases
        radiologistList.add("Not sure")
        radiologistList.add("Brain tumor")
        radiologistList.add("Breast cancer")
        radiologistList.add("Kidney stones")
        radiologistList.add("Liver tumors")
        radiologistList.add("Lung cancer")
        radiologistList.add("Neck pain")
        radiologistList.add("Pancreatic cancer")
        radiologistList.add("Pituitary tumors")
        radiologistList.add("Testicular cancer")
        radiologistList.add("Thyroid cancer")

        //List initializing for Pulmonologist treated diseases
        pulmonologistList.add("Not sure")
        pulmonologistList.add("Asthma")
        pulmonologistList.add("Chest pain or tightness")
        pulmonologistList.add("COVID-19")
        pulmonologistList.add("Interstitial lung disease")
        pulmonologistList.add("Pulmonary hypertension")
        pulmonologistList.add("Tuberculosis")

        //List initializing for Pulmonologist treated diseases
        neurologistList.add("Not sure")
        neurologistList.add("Acute Spinal Cord Injury")
        neurologistList.add("Alzheimer's Disease")
        neurologistList.add("Amyotrophic Lateral Sclerosis")
        neurologistList.add("Brain Tumors")
        neurologistList.add("Cerebral Aneurys")

        //List initializing for Urologists treated diseases
        urologistsList.add("Not sure")
        urologistsList.add("Kidney Stones")
        urologistsList.add("Bladder Infection")
        urologistsList.add("Urinary Retention")
        urologistsList.add("Hematuria")
        urologistsList.add("Erectile Dysfunction")
        urologistsList.add("Prostate Enlargement")
        urologistsList.add("Interstitial Cystitis")

        //List initializing for Otolaryngologist treated diseases
        otolaryngologistsList.add("Not sure")
        otolaryngologistsList.add("Hearing loss")
        otolaryngologistsList.add("Ear infections")
        otolaryngologistsList.add("Balance disorders")
        otolaryngologistsList.add("Diseases of the larynx")
        otolaryngologistsList.add("Nerve pain")
        otolaryngologistsList.add("Facial and cranial nerve disorders")

        //List initializing for Rheumatologists treated diseases
        rheumatologistsList.add("Not sure")
        rheumatologistsList.add("Vasculitis")
        rheumatologistsList.add("Rheumatoid arthritis")
        rheumatologistsList.add("Lupus")
        rheumatologistsList.add("Scleroderma")

        //List initializing for Anesthesiologists treated diseases
        anesthesiologistsList.add("Not sure")
        anesthesiologistsList.add("Back pain or muscle pain")
        anesthesiologistsList.add("Chills caused by low body temperature")
        anesthesiologistsList.add("Difficulty urinating")
        anesthesiologistsList.add("Fatigue")
        anesthesiologistsList.add("Headache")
        anesthesiologistsList.add("Itching")
        anesthesiologistsList.add("Infectious arthritis")

        //List initializing for Gastroenterologist treated diseases
        gastroenterologistsList.add("Not sure")
        gastroenterologistsList.add("Diarrhea")
        gastroenterologistsList.add("Food Poisoning")
        gastroenterologistsList.add("Gas")
        gastroenterologistsList.add("Gastroparesis")
        gastroenterologistsList.add("Irritable Bowel Syndrome")
        gastroenterologistsList.add("Liver Disease")
        gastroenterologistsList.add("Pancreatitis")
        gastroenterologistsList.add("Stomach ache")

        //List initializing for  Allergists treated diseases
        allergistsList.add("Not sure")
        allergistsList.add("Asthma")
        allergistsList.add("Skin Allergies")
        allergistsList.add("Vomiting or diarrhea")
        allergistsList.add("Drop in blood pressure")
        allergistsList.add("Redness of the skin and/or hives")
        allergistsList.add("Difficulty breathing")
        allergistsList.add("Swelling of the throat and/or tongue")

        mapOfDiseasesList["Allergists"] = allergistsList
        mapOfDiseasesList["Anesthesiologists"] = anesthesiologistsList
        mapOfDiseasesList["Cardiologist"] = cardiologistList
        mapOfDiseasesList["Dentist"] = dentistList
        mapOfDiseasesList["ENT specialist"] = entList
        mapOfDiseasesList["Gastroenterologists"] = gastroenterologistsList
        mapOfDiseasesList["Psychiatrists"] = psychiatristsList
        mapOfDiseasesList["Radiologist"] = radiologistList
        mapOfDiseasesList["Pulmonologist"] = pulmonologistList
        mapOfDiseasesList["Neurologist"] = neurologistList
        mapOfDiseasesList["Otolaryngologists"] = otolaryngologistsList
        mapOfDiseasesList["Obstetrician/Gynaecologist"] = gynaecologistList
        mapOfDiseasesList["Orthopaedic surgeon"] = orthopaedicList
        mapOfDiseasesList["Urologists"] = urologistsList
        mapOfDiseasesList["Rheumatologists"] = rheumatologistsList

    }

    private fun setConditionValue(conditionValue: HashMap<String, Float>) {
        conditionValue["Severe Pain"] = 1.5f
        conditionValue["Mild Pain"] = 1.2f
        conditionValue["No Pain"] = 0.5f
    }

    private fun setDiseasesValue(diseasesValue: HashMap<String, Float>) {
        diseasesValue["Not sure"] = 6f

        diseasesValue["High blood pressure"] = 6f
        diseasesValue["High cholesterol"] = 5f
        diseasesValue["Angina (chest pain)"] = 4f
        diseasesValue["Heart rhythm disorders"] = 5f
        diseasesValue["Atrial fibrillation"] = 4f

        diseasesValue["Tooth Decay/Cavities"] = 6f
        diseasesValue["Gum Disease"] = 5f
        diseasesValue["Cracked or Broken Teeth"] = 2f
        diseasesValue["Root Infection"] = 4f
        diseasesValue["Tooth Loss"] = 1f

        diseasesValue["Hearing problems"] = 6f
        diseasesValue["Allergies"] = 4f
        diseasesValue["Nasal congestion"] = 5f
        diseasesValue["Tonsil infections"] = 5f
        diseasesValue["Enlarged tonsils"] = 4f

        diseasesValue["Bleeding during pregnancy"] = 6f
        diseasesValue["Female infertility"] = 5f
        diseasesValue["Heart disease in pregnancy"] = 6f
        diseasesValue["Menopause"] = 4f
        diseasesValue["Menstrual cramps"] = 6f
        diseasesValue["Miscarriage"] = 5f
        diseasesValue["Ovarian cysts"] = 3f
        diseasesValue["Vaginal bleeding"] = 5f

        diseasesValue["Bone fractures"] = 6f
        diseasesValue["Muscle strains"] = 5f
        diseasesValue["Joint or back pain"] = 2f
        diseasesValue["Injuries to tendons or ligaments"] = 5f
        diseasesValue["Limb abnormalities"] = 4f
        diseasesValue["Bone cancer"] = 4f

        diseasesValue["Alcohol use disorder"] = 4f
        diseasesValue["Alzheimer’s disease"] = 6f
        diseasesValue["Anxiety disorders"] = 5f
        diseasesValue["Bipolar disorder"] = 5f
        diseasesValue["Depression"] = 6f
        diseasesValue["Eating disorder"] = 5f
        diseasesValue["Mood disorders"] = 4f
        diseasesValue["Panic disorder"] = 3f
        diseasesValue["Sleep disorders"] = 4f

        diseasesValue["Brain tumor"] = 6f
        diseasesValue["Breast cancer"] = 5f
        diseasesValue["Kidney stones"] = 4f
        diseasesValue["Liver tumors"] = 5f
        diseasesValue["Lung cancer"] = 6f
        diseasesValue["Neck pain"] = 2f
        diseasesValue["Pancreatic cancer"] = 3f
        diseasesValue["Pituitary tumors"] = 5f
        diseasesValue["Testicular cancer"] = 6f
        diseasesValue["Thyroid cancer"] = 5f

        diseasesValue["Asthma"] = 6f
        diseasesValue["Chest pain or tightness"] = 2f
        diseasesValue["COVID-19"] = 3f
        diseasesValue["Interstitial lung disease"] = 3f
        diseasesValue["Pulmonary hypertension"] = 4f
        diseasesValue["Tuberculosis"] = 5f

        diseasesValue["Acute Spinal Cord Injury"] = 6f
        diseasesValue["Amyotrophic Lateral Sclerosis"] = 5f
        diseasesValue["Brain Tumors"] = 6f
        diseasesValue["Cerebral Aneurys"] = 5.5f

        diseasesValue["Kidney Stones"] = 5f
        diseasesValue["Bladder Infection"] = 5f
        diseasesValue["Urinary Retention"] = 2f
        diseasesValue["Hematuria"] = 6f
        diseasesValue["Erectile Dysfunction"] = 6f
        diseasesValue["Prostate Enlargement"] = 5f
        diseasesValue["Interstitial Cystitis"] = 4f

        diseasesValue["Hearing loss"] = 6f
        diseasesValue["Ear infections"] = 5f
        diseasesValue["Balance disorders"] = 4f
        diseasesValue["Diseases of the larynx"] = 3f
        diseasesValue["Nerve pain"] = 6f
        diseasesValue["Facial and cranial nerve disorders"] = 6f

        diseasesValue["Vasculitis"] = 6f
        diseasesValue["Lupus"] = 5f
        diseasesValue["Rheumatoid arthritis"] = 6f
        diseasesValue["Scleroderma"] = 4f

        diseasesValue["Back pain or muscle pain"] = 5f
        diseasesValue["Chills caused by low body temperature"] = 4f
        diseasesValue["Difficulty urinating"] = 6f
        diseasesValue["Fatigue"] = 2f
        diseasesValue["Headache"] = 4f
        diseasesValue["Itching"] = 3f
        diseasesValue["Infectious arthritis"] = 4f

        diseasesValue["Diarrhea"] = 4f
        diseasesValue["Food Poisoning"] = 5f
        diseasesValue["Gas"] = 2f
        diseasesValue["Gastroparesis"] = 4f
        diseasesValue["Irritable Bowel Syndrome"] = 4f
        diseasesValue["Liver Disease"] = 5f
        diseasesValue["Pancreatitis"] = 6f
        diseasesValue["Stomach ache"] = 4f

        diseasesValue["Skin Allergies"] = 2f
        diseasesValue["Vomiting or diarrhea"] = 4f
        diseasesValue["Drop in blood pressure"] = 5f
        diseasesValue["Redness of the skin and/or hive"] = 4f
        diseasesValue["Difficulty breathing"] = 6f
        diseasesValue["Swelling of the throat and/or tongue"] = 5f
    }
}
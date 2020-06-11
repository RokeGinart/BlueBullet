package com.example.coctails.ui.screens.activities.splash

import androidx.annotation.NonNull
import com.example.coctails.core.App
import com.example.coctails.core.Cocktails
import com.example.coctails.core.room.entity.cocktails_data.CocktailFirebaseData
import com.example.coctails.core.room.entity.equipment_data.EquipmentFirebaseData
import com.example.coctails.core.room.entity.glass_data.GlassFirebaseData
import com.example.coctails.core.room.entity.guide_data.GuideFirebaseData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SplashPresenterImpl : SplashPresenter() {

    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.getReference("drink")
    private var glassDatabase = database.getReference("glass")
    private var equipmentDatabase = database.getReference("equipment")
    private var guideDatabase = database.getReference("guides")
    private var databaseVersion = database.getReference("version")

    override fun downloadData() {
        databaseVersion.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                if (Cocktails.getPref().getVersion() != dataSnapshot.value) {
                    Cocktails.getPref().setVersion(dataSnapshot.value as Long)
                    getAllCocktails()
                } else {
                    checkData()
                }
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }

    override fun checkData() {
        addToDispose(
            App.instanse?.database?.cocktailFB()?.getAllFirebaseCocktails()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1.isNotEmpty()) {
                        checkGlassData()
                    } else {
                        getAllCocktails()
                    }
                }
        )
    }

    private fun checkGlassData() {
        addToDispose(
            App.instanse?.database?.glassFB()?.getAllGlass()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1.isNotEmpty()) {
                        checkEquipmentData()
                    } else {
                        getAllGlass()
                    }
                }
        )

    }

    private fun checkEquipmentData() {
        addToDispose(
            App.instanse?.database?.equipmentFB()?.getAllEquipment()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1.isNotEmpty()) {
                        checkGuideData()
                    } else {
                        getAllEquipment()
                    }
                }
        )
    }

    private fun checkGuideData() {
        addToDispose(
            App.instanse?.database?.guideFB()?.getAllGuides()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1.isNotEmpty()) {
                        screenView?.message()
                    } else {
                        getAllGuide()
                    }
                }
        )
    }

    private fun getAllCocktails() {
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {

                App.instanse?.database?.cocktailFB()?.deleteAllData()

                dataSnapshot.children.forEach {
                    it.children.forEach { cocktails ->
                        App.instanse?.database?.cocktailFB()
                            ?.insert(cocktails.getValue(CocktailFirebaseData::class.java)!!)
                    }
                }

                getAllGlass()
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }

    private fun getAllGlass() {
        glassDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                App.instanse?.database?.glassFB()?.deleteAllGlass()

                dataSnapshot.children.forEach {
                    App.instanse?.database?.glassFB()
                        ?.insert(it.getValue(GlassFirebaseData::class.java)!!)
                }

                getAllEquipment()
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }

    private fun getAllEquipment() {
        equipmentDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                App.instanse?.database?.equipmentFB()?.deleteAllEquipment()

                dataSnapshot.children.forEach {
                    App.instanse?.database?.equipmentFB()
                        ?.insert(it.getValue(EquipmentFirebaseData::class.java)!!)
                }

                getAllGuide()
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }

    private fun getAllGuide() {
        guideDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                App.instanse?.database?.guideFB()?.deleteAllGuide()

                dataSnapshot.children.forEach {
                    App.instanse?.database?.guideFB()
                        ?.insert(it.getValue(GuideFirebaseData::class.java)!!)
                }

                screenView?.message()
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }
}
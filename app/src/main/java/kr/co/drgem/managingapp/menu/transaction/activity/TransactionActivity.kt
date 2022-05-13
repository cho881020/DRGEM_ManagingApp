package kr.co.drgem.managingapp.menu.transaction.activity

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityTransactionBinding
import kr.co.drgem.managingapp.menu.transaction.adapter.TransactionAdapter
import kr.co.drgem.managingapp.menu.transaction.dialog.TransactionDialog
import kr.co.drgem.managingapp.menu.transaction.transactionEditListener
import kr.co.drgem.managingapp.models.BasicResponse
import kr.co.drgem.managingapp.models.Georaedetail

class TransactionActivity : BaseActivity(), transactionEditListener {

    lateinit var binding: ActivityTransactionBinding
    lateinit var mAdapter: TransactionAdapter
    var mList: BasicResponse? = null
    val georaedetail2 = ArrayList<Georaedetail>()

    val dialogEdit = TransactionDialog()
//    val dialogDetail = DetailTranDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

        binding.btnFind.setOnClickListener {
            binding.layoutEmpty.isVisible = false
            binding.layoutList.isVisible = true
            binding.layoutInfo.isVisible = true
        }


        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnFold.setOnClickListener {
            binding.layoutFold.isVisible = false
            binding.layoutOpen.isVisible = true
        }

        binding.btnOpen.setOnClickListener {
            binding.layoutFold.isVisible = true
            binding.layoutOpen.isVisible = false
        }

        binding.btnTranRemove.setOnClickListener {
            binding.edtTran.text = null
        }

        binding.btnNameRemove.setOnClickListener {
            binding.edtName.text = null
        }


//        binding.btnEdit.setOnClickListener {
//            georaedetail2.forEach {
//                it.changeSerial=true
//            }
//            mAdapter.notifyDataSetChanged()
//
//            binding.btnEdit.isVisible = false
//            binding.btnFactory.isVisible = false
//            binding.btnSave.isVisible = true
//            binding.btnCancel.isVisible = true
//        }

//        binding.btnCancel.setOnClickListener {
//
//            georaedetail2.forEach {
//                it.changeSerial=false
//            }
//            mAdapter.notifyDataSetChanged()
//
//            binding.btnEdit.isVisible = true
//            binding.btnFactory.isVisible = true
//            binding.btnSave.isVisible = false
//            binding.btnCancel.isVisible = false
//        }


    }

    override fun setValues() {

        mAdapter = TransactionAdapter(this)
        binding.recyclerView.adapter = mAdapter

    }

//    override fun onClickedSearch(dateStart: String, dateEnd: String) {
//
//        setList()
////        mAdapter.setData(mList)
//        binding.layoutBtn.isVisible = true
//    }

    override fun onClickedEdit() {
        dialogEdit.show(supportFragmentManager, "dialog")
    }

    fun setList() {

        georaedetail2.add(
            Georaedetail(
                "14",
                "E08-000601-00",
                "G22042600391",
                "B22042200003",
                "G22042600391",
                "2022-02-03",
                "",
                "",
                "",
                "",
                "",
                "",
            )
        )
        georaedetail2.add(
            Georaedetail(
                "14",
                "E08-000601-00",
                "G22042600391",
                "B22042200003",
                "G22042600391",
                "2022-02-03",
                "",
                "",
                "",
                "",
                "",
                "",
            )
        )
        georaedetail2.add(
            Georaedetail(
                "14",
                "E08-000601-00",
                "G22042600391",
                "B22042200003",
                "G22042600391",
                "2022-02-03",
                "",
                "",
                "",
                "",
                "",
                "",
            )
        )
        georaedetail2.add(
            Georaedetail(
                "14",
                "E08-000601-00",
                "G22042600391",
                "B22042200003",
                "G22042600391",
                "2022-02-03",
                "",
                "",
                "",
                "",
                "",
                "",
            )
        )
        georaedetail2.add(
            Georaedetail(
                "14",
                "E08-000601-00",
                "G22042600391",
                "B22042200003",
                "G22042600391",
                "2022-02-03",
                "",
                "",
                "",
                "",
                "",
                "",
            )
        )



        mList = BasicResponse(
            "000",
            "정상처리되었습니다",
            "G22042600391",
            "20220203",
            "01133",
            "ㅇㅇ전자",
            "00001",
            "구미공장",
            "(구매조건부사업) 연구소 토파즈 정부과제 샘플", "5", georaedetail2
        )

    }




}
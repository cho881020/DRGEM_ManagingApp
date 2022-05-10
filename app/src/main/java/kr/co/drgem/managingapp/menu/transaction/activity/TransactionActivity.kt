package kr.co.drgem.managingapp.menu.transaction.activity

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityTransactionBinding
import kr.co.drgem.managingapp.menu.transaction.EditListener
import kr.co.drgem.managingapp.menu.transaction.SearchListener
import kr.co.drgem.managingapp.menu.transaction.adapter.TransactionAdapter
import kr.co.drgem.managingapp.menu.transaction.dialog.DetailTranDialog
import kr.co.drgem.managingapp.menu.transaction.dialog.EditTranDialog
import kr.co.drgem.managingapp.models.BasicResponse
import kr.co.drgem.managingapp.models.Georaedetail

class TransactionActivity : BaseActivity(),SearchListener, EditListener  {

    lateinit var binding: ActivityTransactionBinding
    lateinit var mAdapter : TransactionAdapter
    var mList : BasicResponse? = null
    val georaedetail2 = ArrayList<Georaedetail>()

    val dialogEdit = EditTranDialog()
    val dialogDetail = DetailTranDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnEdit.setOnClickListener {
            georaedetail2.forEach {
                it.changeSerial=true
            }
            mAdapter.notifyDataSetChanged()

            binding.btnEdit.isVisible = false
            binding.btnFactory.isVisible = false
            binding.btnSave.isVisible = true
            binding.btnCancel.isVisible = true
        }

        binding.btnCancel.setOnClickListener {

            georaedetail2.forEach {
                it.changeSerial=false
            }
            mAdapter.notifyDataSetChanged()

            binding.btnEdit.isVisible = true
            binding.btnFactory.isVisible = true
            binding.btnSave.isVisible = false
            binding.btnCancel.isVisible = false
        }


    }

    override fun setValues() {

        mAdapter = TransactionAdapter(this, this)
        mAdapter.setData(null)
        binding.tradingStatementRecyclerView.adapter = mAdapter

    }

    override fun onClickedSearch(dateStart: String, dateEnd: String) {

        setList()
        mAdapter.setData(mList)
        binding.layoutBtn.isVisible = true
    }

    fun setList(){

        georaedetail2.add(Georaedetail(
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
        ))
        georaedetail2.add(Georaedetail(
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
        ))
        georaedetail2.add(Georaedetail(
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
        ))
        georaedetail2.add(Georaedetail(
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
        ))
        georaedetail2.add(Georaedetail(
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
        ))



        mList = BasicResponse(
            "000",
            "정상처리되었습니다",
            "G22042600391",
            "20220203",
            "01133",
            "ㅇㅇ전자",
            "00001",
            "구미공장",
            "(구매조건부사업) 연구소 토파즈 정부과제 샘플","5", georaedetail2)

    }

    override fun onClickedEdit() {
        dialogEdit.show(supportFragmentManager, "dialog")
    }

    override fun onClickedDetail() {
        dialogDetail.show(supportFragmentManager, "dialog")

    }


}
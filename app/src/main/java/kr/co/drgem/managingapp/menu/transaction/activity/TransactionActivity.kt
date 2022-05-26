package kr.co.drgem.managingapp.menu.transaction.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.ActivityTransactionBinding
import kr.co.drgem.managingapp.menu.transaction.adapter.TransactionAdapter
import kr.co.drgem.managingapp.menu.transaction.dialog.TransactionDialog
import kr.co.drgem.managingapp.menu.transaction.transactionEditListener
import kr.co.drgem.managingapp.models.Detailcode
import kr.co.drgem.managingapp.models.Georaedetail
import kr.co.drgem.managingapp.models.MasterDataResponse
import kr.co.drgem.managingapp.models.TranResponse
import java.text.SimpleDateFormat
import java.util.*

class TransactionActivity : BaseActivity(), transactionEditListener {

    lateinit var binding: ActivityTransactionBinding
    lateinit var mAdapter: TransactionAdapter
    lateinit var detailCode: Detailcode
    val georaedetail = ArrayList<Georaedetail>()
    var mList = TranResponse("", "", "", "", "", "", "", "", "", "", georaedetail)

    val dialogEdit = TransactionDialog()
//    val dialogDetail = DetailTranDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction)


        setValues()
        setupEvents()

        setList()

        sort()

    }

    override fun setupEvents() {


        val bigo = binding.txtBigo.text

        binding.bigo.setOnClickListener {
            AlertDialog.Builder(mContext)
                .setTitle("비고 내용 전체")
                .setMessage(bigo)
                .setNegativeButton("확인", null)
                .show()
        }

        binding.btnSave.setOnClickListener {
            Toast.makeText(mContext, "변경 사항이 저장 되었습니다", Toast.LENGTH_SHORT).show()
        }

        val cal = Calendar.getInstance()
        val dateServer = SimpleDateFormat("yyyyMMdd")  // 서버 전달 포맷
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")     // 텍스트뷰 포맷
        binding.txtDate.text = dateFormat.format(cal.time)


        var calDate = ""
        binding.layoutDate.setOnClickListener {

            val date = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    cal.set(year, month, dayOfMonth)

                    calDate = dateServer.format(cal.time)
                    binding.txtDate.text = dateFormat.format(cal.time)

                }
            }

            val datePick = DatePickerDialog(
                mContext,
                date,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()

        }


        binding.btnFind.setOnClickListener {
            binding.layoutEmpty.isVisible = false
            binding.layoutList.isVisible = true
            binding.layoutInfo.isVisible = true
            binding.layoutFold.isVisible = true
            binding.btnSave.isVisible = true

            mAdapter.setList(georaedetail)
        }



        binding.btnBack.setOnClickListener {

            backDialog()

        }

        binding.btnFold.setOnClickListener {
            binding.layoutFold.isVisible = false
            binding.btnOpen.isVisible = true
        }

        binding.btnOpen.setOnClickListener {
            binding.layoutFold.isVisible = true
            binding.btnOpen.isVisible = false
        }

        binding.btnTranRemove.setOnClickListener {
            binding.edtTran.text = null
        }

        binding.btnNameRemove.setOnClickListener {
            binding.edtName.text = null
        }


    }

    override fun setValues() {

        mAdapter = TransactionAdapter(this)
        binding.recyclerView.adapter = mAdapter

        val masterData = intent.getSerializableExtra("masterData") as MasterDataResponse


        val spinnerCompanyAdapter =
            MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, masterData.getCompanyCode())
        binding.spinnerCompany.adapter = spinnerCompanyAdapter


        val spinnerWareHouseAdapter =
            MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, arrayListOf())
        binding.spinnerWareHouse.adapter = spinnerWareHouseAdapter

        binding.spinnerCompany.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (masterData.getCompanyCode()[position].code == "0001") {
                        spinnerWareHouseAdapter.setList(masterData.getGwangmyeongCode())
                    }

                    if (masterData.getCompanyCode()[position].code == "0002") {
                        spinnerWareHouseAdapter.setList(masterData.getGumiCode())
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
    }



    fun sort() {

        var onClickSeq = 0

        binding.layoutSeq.setOnClickListener {

            if (onClickSeq < 2) {
                onClickSeq++
            } else {
                onClickSeq = 0
            }

            when (onClickSeq) {

                0 -> {
                    binding.imgSeq.setImageResource(R.drawable.dropempty)
                    mAdapter.setList(mList.georaedetail)

                }

                1 -> {
                    binding.imgSeq.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(mList.getDownSeq())

                }

                2 -> {
                    binding.imgSeq.setImageResource(R.drawable.dropup)
                    mAdapter.setList(mList.getUpSeq())
                }
            }

        }

        var onClickLocation = 0

        binding.layoutLocation.setOnClickListener {

            if (onClickLocation < 2) {
                onClickLocation++
            } else {
                onClickLocation = 0
            }

            when (onClickLocation) {

                0 -> {
                    binding.imgLocation.setImageResource(R.drawable.dropempty)
                    mAdapter.setList(mList.georaedetail)
                }

                1 -> {
                    binding.imgLocation.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(mList.getDownLocation())
                }

                2 -> {
                    binding.imgLocation.setImageResource(R.drawable.dropup)
                    mAdapter.setList(mList.getUpLocation())
                }
            }

        }
    }


    override fun onClickedEdit() {
        dialogEdit.show(supportFragmentManager, "dialog")
    }


    override fun onBackPressed() {

        backDialog()

    }

    fun setList() {

        georaedetail.add(
            Georaedetail(
                "2",
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
                "서울",
            )
        )
        georaedetail.add(
            Georaedetail(
                "3",
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
                "구미",
            )
        )
        georaedetail.add(
            Georaedetail(
                "4",
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
                "대구",
            )
        )
        georaedetail.add(
            Georaedetail(
                "5",
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
                "구미",
            )
        )
        georaedetail.add(
            Georaedetail(
                "1",
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
                "서울",
            )
        )



        mList = TranResponse(
            "000",
            "정상처리되었습니다",
            "G22042600391",
            "20220203",
            "01133",
            "ㅇㅇ전자",
            "00001",
            "구미공장",
            "(구매조건부사업) 연구소 토파즈 정부과제 샘플", "5", georaedetail
        )

    }


}
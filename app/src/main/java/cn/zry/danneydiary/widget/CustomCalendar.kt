package cn.zry.danneydiary.widget

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import cn.zry.danneydiary.R
import cn.zry.danneydiary.utils.DateUtils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.HashMap


/**
 * author : openXu
 * create at : 2017/1/3 9:56
 * last modified by zry at 2018/1/27
 * blog : http://blog.csdn.net/xmxkf
 * gitHub : https://github.com/openXu
 * project : CustomCalendar
 * class name : CustomCalendar
 * version : 1.0
 * class describe：自定义日历控件
 */
class CustomCalendar : View {

    private val TAG = "CustomCalendar"

    /**
     * 各部分背景
     */
    private val mBgMonth: Int
    private val mBgWeek: Int
    private val mBgDay: Int
    private val mBgSuf: Int
    /**
     * 标题的颜色、大小
     */
    private val mTextColorMonth: Int
    private val mTextSizeMonth: Float
    private val mToday: Int
    private val mMonthRowL: Int
    private val mMonthRowR: Int
    private val mMonthRowSpac: Float
    private val mMonthSpac: Float
    /**
     * 星期的颜色、大小
     */
    private val mTextColorWeek: Int
    private val mSelectWeekTextColor: Int
    private val mTextSizeWeek: Float
    /**
     * 日期文本的颜色、大小
     */
    private val mTextColorDay: Int
    private val mTextSizeDay: Float
    /**
     * 下标次数文本的颜色、大小
     */
    private val mTextColorSufFormer: Int
    private val mTextColorSufLatter: Int
    private val mTextColorSufNull: Int
    private val mTextSizeSuf: Float
    /**
     * 选中的文本的颜色
     */
    private val mSelectTextColor: Int
    /**
     * 选中背景
     */
    private val mSelectBg: Int
    private val mCurrentBg: Int
    private val mSelectRadius: Float
    private val mCurrentBgStrokeWidth: Float
    private var mCurrentBgDashPath: FloatArray? = null
    /**
     * 行间距
     */
    private val mLineSpac: Float
    /**
     * 字体上下间距，高度
     */
    private val mTextSpac: Float
    private var titleHeight: Float = 0.toFloat()
    private var weekHeight: Float = 0.toFloat()
    private var dayHeight: Float = 0.toFloat()    //日期高度
    private var sufHeight: Float = 0.toFloat()    //下标高度
    private var dateCellH: Float = 0.toFloat()    //每行高度
    private var dateCellW: Float = 0.toFloat()    //每列宽度

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr) {
        //获取自定义属性的值
        val a = context.theme.obtainStyledAttributes(
                attrs, R.styleable.CustomCalendar, defStyleAttr, 0)

        mBgMonth = a.getColor(R.styleable.CustomCalendar_mBgMonth, Color.TRANSPARENT)
        mBgWeek = a.getColor(R.styleable.CustomCalendar_mBgWeek, Color.TRANSPARENT)
        mBgDay = a.getColor(R.styleable.CustomCalendar_mBgDay, Color.TRANSPARENT)
        mBgSuf = a.getColor(R.styleable.CustomCalendar_mBgSuf, Color.TRANSPARENT)

        mToday = a.getResourceId(R.styleable.CustomCalendar_mToday, R.drawable.custom_calendar_today)
        mMonthRowL = a.getResourceId(R.styleable.CustomCalendar_mMonthRowL, R.drawable.custom_calendar_row_left)
        mMonthRowR = a.getResourceId(R.styleable.CustomCalendar_mMonthRowR, R.drawable.custom_calendar_row_right)
        mMonthRowSpac = a.getDimension(R.styleable.CustomCalendar_mMonthRowSpac, 20f)
        mTextColorMonth = a.getColor(R.styleable.CustomCalendar_mTextColorMonth, Color.BLACK)
        mTextSizeMonth = a.getDimension(R.styleable.CustomCalendar_mTextSizeMonth, 100f)
        mMonthSpac = a.getDimension(R.styleable.CustomCalendar_mMonthSpac, 20f)
        mTextColorWeek = a.getColor(R.styleable.CustomCalendar_mTextColorWeek, Color.BLACK)
        mSelectWeekTextColor = a.getColor(R.styleable.CustomCalendar_mSelectWeekTextColor, Color.BLACK)

        mTextSizeWeek = a.getDimension(R.styleable.CustomCalendar_mTextSizeWeek, 70f)
        mTextColorDay = a.getColor(R.styleable.CustomCalendar_mTextColorDay, Color.GRAY)
        mTextSizeDay = a.getDimension(R.styleable.CustomCalendar_mTextSizeDay, 70f)
        mTextColorSufFormer = a.getColor(R.styleable.CustomCalendar_mTextColorSufFormer, Color.BLUE)
        mTextColorSufLatter = a.getColor(R.styleable.CustomCalendar_mTextColorSufLatter, Color.BLUE)
        mTextColorSufNull = a.getColor(R.styleable.CustomCalendar_mTextColorSufNull, Color.BLUE)
        mTextSizeSuf = a.getDimension(R.styleable.CustomCalendar_mTextSizeSuf, 40f)
        mSelectTextColor = a.getColor(R.styleable.CustomCalendar_mSelectTextColor, Color.YELLOW)
        mCurrentBg = a.getColor(R.styleable.CustomCalendar_mCurrentBg, Color.GRAY)
        try {
            val dashPathId = a.getResourceId(R.styleable.CustomCalendar_mCurrentBgDashPath,
                    R.array.customCalendar_currentDay_bg_DashPath)
            val array = resources.getIntArray(dashPathId)
            mCurrentBgDashPath = array.map { item -> item.toFloat() }.toFloatArray()
        } catch (e: Exception) {
            e.printStackTrace()
            mCurrentBgDashPath = floatArrayOf(2f, 3f, 2f, 3f)
        }

        mSelectBg = a.getColor(R.styleable.CustomCalendar_mSelectBg, Color.YELLOW)
        mSelectRadius = a.getDimension(R.styleable.CustomCalendar_mSelectRadius, 20f)
        mCurrentBgStrokeWidth = a.getDimension(R.styleable.CustomCalendar_mCurrentBgStrokeWidth, 5f)
        mLineSpac = a.getDimension(R.styleable.CustomCalendar_mLineSpac, 20f)
        mTextSpac = a.getDimension(R.styleable.CustomCalendar_mTextSpac, 20f)
        a.recycle()  //注意回收

        initCompute()

        //默认显示当前月份
        currentDate = Calendar.getInstance()
        currentDate.time = (DateUtils.date2Date(Date()))
        selectDate = Calendar.getInstance()
        selectDate.time = currentDate.time
        setMonth(DateUtils.date2MonthStr(currentDate.time),true)
    }

    /**
     * 计算相关常量，构造方法中调用
     */
    private lateinit var mPaint: Paint
    private lateinit var bgPaint: Paint
    private fun initCompute() {
        mPaint = Paint()
        bgPaint = Paint()
        mPaint.isAntiAlias = true //抗锯齿
        bgPaint.isAntiAlias = true //抗锯齿

        //标题高度
        mPaint.textSize = mTextSizeMonth
        titleHeight = FontUtil.getFontHeight(mPaint) + 2 * mMonthSpac
        //星期高度
        mPaint.textSize = mTextSizeWeek
        weekHeight = FontUtil.getFontHeight(mPaint)
        //日期高度
        mPaint.textSize = mTextSizeDay
        dayHeight = FontUtil.getFontHeight(mPaint)
        //下标字体高度
        mPaint.textSize = mTextSizeSuf
        sufHeight = FontUtil.getFontHeight(mPaint)
        //每行高度 = 行间距 + 日期字体高度 + 字间距 + 次数字体高度
        dateCellH = mLineSpac + dayHeight + mTextSpac + sufHeight

    }

    /**
     * 日期信息
     */
    private var displayMonth: String = "" //展示的年月(yyyy年MM月)
    private lateinit var displayMonthDate: Date
    private val currentDate: Calendar //当前日期
    private val selectDate: Calendar //选中的日期
    private val WEEK_STR = arrayOf("一", "二", "三", "四", "五", "六", "日")

    private var info_dayOfMonth: Int = 0    //月份天数
    private var info_firstWeekIndex: Int = 0    //当月第一天位置索引
    private var info_lineNum: Int = 0      //日期行数

    /**
     * 设置年和月份(翻页)，计算日期信息
     */
    private fun setMonth(monthStr: String, isInitial:Boolean=false) {
        if (displayMonth.equals(monthStr)) return
        this.displayMonth = monthStr
        this.displayMonthDate = DateUtils.str2Month(displayMonth)
        // 计算日期信息
        val month = Calendar.getInstance()
        month.time = DateUtils.str2Month(monthStr)
        info_dayOfMonth = month.getActualMaximum(Calendar.DAY_OF_MONTH)
        info_firstWeekIndex = (month.get(Calendar.DAY_OF_WEEK) - 2 + 7) % 7
        info_lineNum = Math.ceil((info_dayOfMonth + info_firstWeekIndex).toDouble() / 7.0).toInt()
        // 重载数据, 初始化时不加载数据(因此时listener未初始化)
        if(!isInitial)listener.loadData(monthStr)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //宽度 = 填充父窗体
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)   //获取宽的尺寸
        dateCellW = widthSize.toFloat() / 7
        //高度 = 标题高度 + 星期高度 + 日期行数*每行高度
        val height = titleHeight + weekHeight + info_lineNum * dateCellH
        setMeasuredDimension(View.getDefaultSize(suggestedMinimumWidth, widthMeasureSpec),
                height.toInt())

    }

    override fun onDraw(canvas: Canvas) {
        drawTitleBar(canvas)
        drawWeekBar(canvas)
        drawDayTable(canvas)
    }

    private var monthTextStart: Float = 0.toFloat()
    private var monthTextWidth: Float = 0.toFloat()
    private var todayBtnStart: Float = 0.toFloat()
    private var todayBtnWidth: Float = 0.toFloat()
    private var rowLStart: Float = 0.toFloat()
    private var rowRStart: Float = 0.toFloat()
    private var rowWidth: Float = 0.toFloat()

    /**
     * 绘制标题，包括年月、今天按钮、翻页按钮
     */
    private fun drawTitleBar(canvas: Canvas) {
        // 背景
        bgPaint.color = mBgMonth
        val rect = RectF(0f, 0f, width.toFloat(), titleHeight)
        canvas.drawRect(rect, bgPaint)
        //今天按钮的图片
        var bitmap = BitmapFactory.decodeResource(resources, mToday)
        todayBtnWidth = bitmap.width.toFloat()
        // 绘制月份
        mPaint.textSize = mTextSizeMonth
        mPaint.color = mTextColorMonth
        monthTextWidth = mPaint.measureText(displayMonth)
        monthTextStart = (width - monthTextWidth - todayBtnWidth - 30) / 2
        canvas.drawText(displayMonth, monthTextStart,
                mMonthSpac + FontUtil.getFontLeading(mPaint), mPaint)
        // 绘制今天按钮
        todayBtnStart = monthTextStart + monthTextWidth + 30
        canvas.drawBitmap(bitmap, todayBtnStart, (titleHeight - bitmap.height) / 2, Paint())
        // 绘制左右箭头
        bitmap = BitmapFactory.decodeResource(resources, mMonthRowL)
        rowWidth = bitmap.width.toFloat()
        rowLStart = monthTextStart - mMonthRowSpac - rowWidth
        canvas.drawBitmap(bitmap, rowLStart, (titleHeight - bitmap.height) / 2, Paint())
        bitmap = BitmapFactory.decodeResource(resources, mMonthRowR)
        rowRStart = todayBtnStart + todayBtnWidth + mMonthRowSpac
        canvas.drawBitmap(bitmap, rowRStart, (titleHeight - bitmap.height) / 2, Paint())
    }

    /**
     * 绘制星期表头
     */
    private fun drawWeekBar(canvas: Canvas) {
        //背景
        bgPaint.color = mBgWeek
        val rect = RectF(0f, titleHeight, width.toFloat(), titleHeight + weekHeight)
        canvas.drawRect(rect, bgPaint)
        //绘制星期：七天
        mPaint.textSize = mTextSizeWeek
        val selectMonth = DateUtils.date2Str(selectDate.time)
        val selectWeekIndex = (selectDate.get(Calendar.DAY_OF_WEEK) - 2) % 7
        for (i in WEEK_STR.indices) {
            if (selectMonth.equals(displayMonth) && selectWeekIndex == i) {
                mPaint.color = mSelectWeekTextColor
            } else {
                mPaint.color = mTextColorWeek
            }
            val x = i * dateCellW + (dateCellW - mPaint.measureText(WEEK_STR[i])) / 2
            canvas.drawText(WEEK_STR[i], x, titleHeight + FontUtil.getFontLeading(mPaint), mPaint)
        }
    }

    /**
     * 绘制日期和下标
     */
    private fun drawDayTable(canvas: Canvas) {
        val paintDate = Calendar.getInstance()
        paintDate.time = displayMonthDate
        var left = info_firstWeekIndex * dateCellW
        var top = titleHeight + weekHeight + mLineSpac
        //获取笔粗细的偏移量
        mPaint.textSize = mTextSizeDay
        val dayTextLeading = FontUtil.getFontLeading(mPaint)
        mPaint.textSize = mTextSizeSuf
        val sufTextLeading = FontUtil.getFontLeading(mPaint)
        for (i in 1..info_dayOfMonth) {
            val dayStr = i.toString()
            val dayBottom = top + dayHeight
            val sufTop = dayBottom + mTextSpac
            val sufBottom = sufTop + sufHeight
            //背景
            if (i==1 || left-0 <= 1e-5) {
                bgPaint.color = mBgDay
                var dayBgRect = RectF(0f, top, width.toFloat(), dayBottom + mTextSpac)
                if (i==1)
                    dayBgRect = RectF(0f, top-mLineSpac, width.toFloat(), dayBottom + mTextSpac)
                canvas.drawRect(dayBgRect, bgPaint)
                bgPaint.color = mBgSuf
                val sufBgrect = RectF(0f, sufTop, width.toFloat(), sufBottom + mLineSpac)
                canvas.drawRect(sufBgrect, bgPaint)
            }
            //如果是当天日期
            if (paintDate.time.equals(currentDate.time)) {
                bgPaint.color = mCurrentBg
                bgPaint.style = Paint.Style.STROKE  //空心
                val effect = DashPathEffect(mCurrentBgDashPath, 1f)
                bgPaint.pathEffect = effect   //设置画笔曲线间隔
                bgPaint.strokeWidth = mCurrentBgStrokeWidth       //画笔宽度
                //绘制空心圆背景
                canvas.drawCircle((left + dateCellW / 2).toFloat(), top + dayHeight / 2,
                        mSelectRadius - mCurrentBgStrokeWidth, bgPaint)
                //绘制完后将画笔还原，避免脏笔
                bgPaint.pathEffect = null
                bgPaint.strokeWidth = 0f
                bgPaint.style = Paint.Style.FILL
            }
            //如果是选中的日期
            if (paintDate.time.equals(selectDate.time)) {
                mPaint.color = mSelectTextColor
                bgPaint.color = mSelectBg
                //绘制橙色圆背景，参数一是中心点的x轴，参数二是中心点的y轴，参数三是半径，参数四是paint对象；
                canvas.drawCircle((left + dateCellW / 2).toFloat(),
                        top + dayHeight / 2,
                        mSelectRadius, bgPaint)
            } else {
                mPaint.color = mTextColorDay
            }
            //写日期
            mPaint.textSize = mTextSizeDay
            var x = left + (dateCellW - mPaint.measureText(dayStr)) / 2
            canvas.drawText(dayStr, x, top + dayTextLeading, mPaint)
            //绘制日期下标
            mPaint.textSize = mTextSizeSuf
            val dayEval = dateDatamap[paintDate.timeInMillis]
            if (dayEval != null) {
                val sufStrLen = mPaint.measureText("${dayEval.commendCount}/${dayEval.criticizeCount}")
                mPaint.color = mTextColorSufFormer
                x = left + (dateCellW - sufStrLen) / 2
                canvas.drawText(dayEval.commendCount.toString(),
                        x, sufTop + sufTextLeading, mPaint)
                mPaint.color = mTextColorSufNull
                x += mPaint.measureText(dayEval.commendCount.toString())
                canvas.drawText("/",
                        x, sufTop + sufTextLeading, mPaint)
                mPaint.color = mTextColorSufLatter
                x += mPaint.measureText("/")
                canvas.drawText(dayEval.criticizeCount.toString(),
                        x, sufTop + sufTextLeading, mPaint)
            } else {
                mPaint.color = mTextColorSufNull
                val sufStr = "0/0"
                x = left + (dateCellW - mPaint.measureText(sufStr)) / 2
                canvas.drawText(sufStr, x, sufTop + sufTextLeading, mPaint)
            }
            // 循环下一个日期
            paintDate.add(Calendar.DAY_OF_MONTH, 1)
            left += dateCellW
            if (7 * dateCellW - left <= 1e-5) { //换行
                left = 0f
                top += dateCellH
            }

        }
    }


    /****************************事件处理↓↓↓↓↓↓↓ */
    //焦点坐标
    private var lastTouchX = 0f
    private var lastTouchY = 0f

    //控制事件是否响应
    private var responseWhenEnd = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action and MotionEvent.ACTION_MASK
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchX = event.x
                lastTouchY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_OUTSIDE, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                val moveDistanceX = event.x - lastTouchX
                val moveDistanceY = event.y - lastTouchY
                if (moveDistanceX > 100) //向右滑动
                    listener.onLeftRowClick()
                else if (moveDistanceX < -100) //向左滑动
                    listener.onRightRowClick()
                else //点击
                    touchHandle(PointF(event.x, event.y))
            }
        }
        return true
    }

    /**
     * 处理点击事件
     */
    fun touchHandle(point: PointF) {
        if (point.y <= titleHeight) {
            //事件在标题上
            if (point.x >= rowLStart && point.x < rowLStart + rowWidth) {
                //点击左箭头
                listener.onLeftRowClick()
            } else if (point.x > rowRStart && point.x < rowRStart + rowWidth) {
                //点击右箭头
                listener.onRightRowClick()
            } else if (point.x >= monthTextStart && point.x < monthTextStart + monthTextWidth) {
                //点击年月标题
                listener.onTitleClick(displayMonth, displayMonthDate)
            } else if (point.x >= todayBtnStart && point.x < todayBtnStart + todayBtnWidth) {
                //点击今天按钮
                listener.onTodayClick()
            }
        } else if (point.y <= titleHeight + weekHeight) {
            //事件在星期部分
            var xIndex = Math.floor((point.x / dateCellW).toDouble()).toInt()
            listener.onWeekClick(xIndex, WEEK_STR[xIndex])
        } else {
            //事件在日期部分
            val rowIndex: Int = Math.floor(((
                    point.y - titleHeight - weekHeight) / dateCellH
                    ).toDouble()).toInt()
            val colIndex: Int = Math.floor((point.x / dateCellW).toDouble()).toInt()
            val selectDayInt = rowIndex * 7 - info_firstWeekIndex + colIndex + 1
            if (selectDayInt > 0 && selectDayInt <= info_dayOfMonth) {
                val selectDateStr = "${displayMonth}${selectDayInt}日"
                listener.onDayClick(selectDayInt - 1, selectDateStr, null)
            }
        }
    }

    /****************************事件处理↑↑↑↑↑↑↑ */

    override fun invalidate() {
        requestLayout()
        super.invalidate()
    }

    /***********************接口API↓↓↓↓↓↓↓ */
    private var dateDatamap: MutableMap<Long, DayEvaluation> = HashMap()

    private var listener: onClickListener = simpleOnClickListener(this)

    fun initData(){
        listener.loadData(displayMonth)
        listener.onDayClick(
          currentDate.get(Calendar.DAY_OF_MONTH),
          DateUtils.date2Str(currentDate.time),
          dateDatamap[currentDate.timeInMillis]
        )
    }
    fun setEvaluation(map: Map<Long, DayEvaluation>) {
        dateDatamap.clear()
        dateDatamap.putAll(map)
        invalidate()
    }
    fun refresh(){
        listener.loadData(displayMonth)
        listener.onDayClick(
          selectDate.get(Calendar.DAY_OF_MONTH),
          DateUtils.date2Str(selectDate.time),
          dateDatamap[selectDate.timeInMillis]
        )
        invalidate()
    }

    /**
     * 更新当前日期
     */
    fun updateCurrentDate(){
        currentDate.time = DateUtils.date2Date(Date())
    }

    /**
     * 变更月份(翻页)
     */
    fun changeMonth(amount: Int) {
        if (amount == 0) return
        val monthCalendar = Calendar.getInstance()
        monthCalendar.time = displayMonthDate
        monthCalendar.add(Calendar.MONTH, amount)
        setMonth(DateUtils.date2MonthStr(monthCalendar.time))
    }

    fun changeMonth(dateStr: String) = changeMonth(DateUtils.str2Date(dateStr))

    fun changeMonth(date: Date) {
        setMonth(DateUtils.date2MonthStr(date))
    }

    /**
     * 选择日期
     */
    fun changeSelectDate(date: Date) = changeSelectDate(DateUtils.date2Str(date))

    fun changeSelectDate(dateStr: String) {
        changeMonth(dateStr)
        selectDate.time = DateUtils.str2Date(dateStr)
    }

    fun setOnClickListener(listener: onClickListener) {
        this.listener = listener
    }

    interface onClickListener {

        fun loadData(monthStr: String)

        fun onLeftRowClick()

        fun onRightRowClick()

        fun onTodayClick()

        fun onTitleClick(titleStr: String, month: Date)

        fun onWeekClick(weekIndex: Int, weekStr: String)

        fun onDayClick(dayIndex: Int, dayStr: String, dayEval: DayEvaluation?)
    }

    public open class simpleOnClickListener(val calendar: CustomCalendar) : onClickListener {

        override fun loadData(monthStr: String) {}

        override fun onLeftRowClick() {
            calendar.changeMonth(-1)
            calendar.invalidate()
        }

        override fun onRightRowClick() {
            calendar.changeMonth(1)
            calendar.invalidate()
        }

        override fun onTodayClick() {
            calendar.updateCurrentDate()
            onDayClick(
              calendar.currentDate.get(Calendar.DAY_OF_MONTH),
              DateUtils.date2Str(calendar.currentDate.time),
              calendar.dateDatamap[calendar.currentDate.timeInMillis]
            )
            calendar.invalidate()
        }

        override fun onTitleClick(titleStr: String, month: Date) {}

        override fun onWeekClick(weekIndex: Int, weekStr: String) {}

        override fun onDayClick(dayIndex: Int, dayStr: String, dayEval: DayEvaluation?) {
            calendar.changeSelectDate(dayStr)
            calendar.invalidate()
        }
    }

    class DayEvaluation(var date: Date, var commendCount: Int, var criticizeCount: Int)


    /***********************接口API↑↑↑↑↑↑↑ */

    private object FontUtil {
        /**
         * @return 返回指定笔的文字高度
         */
        fun getFontHeight(paint: Paint?): Float {
            val fm = paint!!.fontMetrics
            return fm.descent - fm.ascent
        }

        /**
         * @return 返回指定笔离文字顶部的基准距离
         */
        fun getFontLeading(paint: Paint?): Float {
            val fm = paint!!.fontMetrics
            return fm.leading - fm.ascent
        }


    }
}

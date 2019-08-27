package com.dike.test.twhomework.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;


import com.dike.test.twhomework.R;

import java.lang.ref.WeakReference;

public class PullToRefreshView extends RelativeLayout implements OnTouchListener
{
	private static final int SHOW_HEADER_TYPE_AUTO 				= 1;
	private static final int SHOW_HEADER_TYPE_FORCE_SHOW 		= 2;
	private static final int SHOW_HEADER_TYPE_FORCE_HIDDEN 		= 3;
	
	public static final int PULL_TYPE_NULL						= 10;
	public static final int PULL_TYPE_BOTH						= 11;
	public static final int PULL_TYPE_DOWN						= 12;
	public static final int PULL_TYPE_UP						= 13;
	
	public static final int PULL_DIRECTION_DOWN 				= 14;
	public static final int PULL_DIRECTION_UP 					= 15;
	
	public static final int MSG_WAHT_ON_REFRESH					= 16;
	public static final int MSG_WAHT_ON_PRE_REFRESH				= 17;
	
	/**
	 * 可滑动
	 */
	private static final int SCROLL_STATE_ENABLE 				= 1;
	/**
	 * 不可滑动
	 */
	private static final int SCROLL_STATE_DISENABLE 			= 2;
	/**
	 * 未知
	 */
	private static final int SCROLL_STATE_UNKNOW				= 3;
	
	
	/**
	 * 滑动的类型。包括（仅下拉刷新，仅上拉刷新，同时上拉下拉刷新，无）
	 */
	private int mPullType;
	/**
	 * 用于滑动的scroller
	 */
	private Scroller mScroller; 
	/**
	 * 顶部滑动拉出需要显示的view
	 */
	private View mHeadView;  
	private TextView mHeadViewTV;
	private TextView mSubHeadViewTV;
	private ImageView mHeadViewIV;
	private ProgressBar mHeadViewPB;
	/**
	 * 中间显示的触发滑动刷新的主体view，如listview
	 */
	private View mContentPullFireView;
	
	/**
	 * 中间显示的内容view，其中可包含一个触发滑动刷新的主体view，也可以自己本身就是一个触发滑动刷新的主体view
	 */
	private View mContentView;
	/**
	 * 底部滑动拉出需要显示的view
	 */
	private View mFootView;  
	private TextView mFootViewTV;
	private TextView mSubFootViewTV;
	private ImageView mFootViewIV;
	private ProgressBar mFootViewPB;
	
	/**
	 * 外部委任来接管touch事件
	 */
	private OuterTouchDelegate mOuterTouchDelegate;
	
	/**
	 * headview可拉出的最大高度
	 */
	private int maxHeadViewPullOutHeight;
	
	/**
	 * footview可拉出的最大高度
	 */
	private int maxFootViewPullOutHeight;
	
	
	/**
	 * headview拉出视为可以刷新listview的最小高度
	 */
	private int minRefreshHeadViewHeight;
	
	/**
	 * footview拉出视为可以刷新listview的最小高度
	 */
	private int minRefreshFootViewHeight;
	
	
	/**
	 * HeadView的显示实际高度/宽度.
	 */
	private int mHeadViewHeight;
	
	/**
	 * FootView的显示实际高度/宽度.
	 */
	private int mFootViewHeight;
	
	private double  mRefreshHeadViewRatio = 1;
	private double  mRefreshFootViewRatio = 1;
	private double  mVerticalSlope =  0.8; //40度
	
	/**
	 * 拉动的方向。【默认方向向下】
	 */
	private int mPullDirection;
	
	/**
	 * 标示headview状态是否是正在刷新数据。
	 */
	private boolean isHeadViewRefreshing;

	/**
	 * 标示footview状态是否是正在刷新数据
	 */
	private boolean isFootViewRefreshing;
	/**
	 * 标记当前的headview或footview是否显示出来了
	 */
	private boolean isPullViewShow;
	
	private boolean isHeadViewShow,isFootViewShow;
	
	/**
	 * 是否禁用滑动刷新效果。
	 */
	private boolean isScrollable = true;
	
	/**
	 * 标记是否touch在header或footer上
	 */
	private boolean isTouchOnPullView = false;
	
	/**
	 * 标记当touch在header或footer上时是否触发滑动
	 */
	private boolean isTouchPullViewCanScroll = false;
	
	/**
	 * 初始化时是否测量mPullView的高度
	 */
	private boolean isMeasurePullView = true;
	
	/**
	 * 当前的滑动状态，可滑动，不可滑动，未知
	 */
	private int mScrollState = SCROLL_STATE_UNKNOW;
	
	/**
	 * 是否显示拖动的轨迹
	 */
	private boolean isShowMovePath = true;
	
	private int mShowPullViewType = SHOW_HEADER_TYPE_AUTO;
	
	/**
	 * 是否强制显示或隐藏
	 */
	private boolean isForceHidden,isForceShow;
	
	private  int mLastMotionY,mLastMotionX;
	
	private int mCurrentDistance,distance;
	
	private int mDuration;
	
	private float mLastDownY = 0f;
	
	private boolean isCanRefreshListView = false;
	
	private LayoutInflater mInflater;
	private ScrollInsterface mScrollInterface;
	private MyHandler mHandler;
	
	
	private RotateAnimation mHeadAnimation;
	private RotateAnimation mHeadReverseAnimation;
	private RotateAnimation mFootAnimation;
	private RotateAnimation mFootReverseAnimation;
	
	
	public static interface ScrollInsterface
	{
		/**
		 * 开始刷新数据
		 * @param pullView TODO 
		 * @param direction
		 */
		public void onRefreshView(PullToRefreshView pullView, int direction);
		
		/**
		 * onRefreshView之前回调，释放将会启动onRefreshView(int direction)方法,但是不一定会触发onRefreshView(int direction)方法
		 * @param pullView TODO
		 * @param direction
		 */
		public void onPreRefreshView(PullToRefreshView pullView, int direction);
	}
	
	public static abstract class DefaultScrollInsterfaceImp implements ScrollInsterface
	{

		@Override
		public void onRefreshView(PullToRefreshView pullView, int direction)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPreRefreshView(PullToRefreshView pullView, int direction) 
		{
			// TODO Auto-generated method stub
			
		}
		
		
	}
	public static interface OuterTouchDelegate
	{
		/**
		 * 由外部委任来时接管touch事件
		 * @param event
		 * @return true为已消耗此事件；反之false
		 */
		public boolean onTouchEvent(MotionEvent event);
	}
	private static class MyHandler extends Handler
	{

		WeakReference<PullToRefreshView> mScrollLinerWR;
		
		public MyHandler(PullToRefreshView srollLiner)
		{
			mScrollLinerWR = new WeakReference<PullToRefreshView>(srollLiner);
		}
		
		@Override
		public void handleMessage(Message msg)
		{
			if(null != mScrollLinerWR && null != mScrollLinerWR.get())
			{
				PullToRefreshView sl = mScrollLinerWR.get();
				sl.isForceHidden = false;
				sl.isForceShow   = false;
				switch(msg.what)
				{
				case MSG_WAHT_ON_REFRESH:
					if(null != sl.mScrollInterface)
					{
						sl.mScrollInterface.onRefreshView(sl, msg.arg1);
					}
					break;
				case MSG_WAHT_ON_PRE_REFRESH:
					if(null != sl.mScrollInterface)
					{
						sl.mScrollInterface.onPreRefreshView(sl, msg.arg1);
					}
					break;
				default:
					
					break;
				}
			}
		}
	}

	
	public PullToRefreshView(Context context)
	{
		super(context);
		init(context);
	}

	public PullToRefreshView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context)
	{
		mInflater = LayoutInflater.from(context);
		mScroller = new Scroller(context);
		mHandler  = new MyHandler(this);
		mDuration = 800;
		mPullType = PULL_TYPE_UP;
		isHeadViewRefreshing = false;
		isFootViewRefreshing = false;
		isHeadViewShow = false;
		isFootViewShow = false;
		isPullViewShow = false;
//		setOrientation(VERTICAL);
		
		mHeadAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mHeadAnimation.setInterpolator(new LinearInterpolator());
		mHeadAnimation.setDuration(200);
		mHeadAnimation.setFillAfter(true);

		mHeadReverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mHeadReverseAnimation.setInterpolator(new LinearInterpolator());
		mHeadReverseAnimation.setDuration(200);
		mHeadReverseAnimation.setFillAfter(true);
		
		mFootAnimation = new RotateAnimation(0, 180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mFootAnimation.setInterpolator(new LinearInterpolator());
		mFootAnimation.setDuration(200);
		mFootAnimation.setFillAfter(true);

		mFootReverseAnimation = new RotateAnimation(180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mFootReverseAnimation.setInterpolator(new LinearInterpolator());
		mFootReverseAnimation.setDuration(200);
		mFootReverseAnimation.setFillAfter(true);
		
		setupView();
		
	}
	
	private void measureView(View child) 
	{
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) 
		{
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) 
		{
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else 
		{
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}
	
	/**
	 * 重新设置pullview的宽度或高度
	 * @param height
	 */
	public void requestHeadViewLayoutHeight(int height)
	{
		setRefreshHeadViewRatio(mRefreshHeadViewRatio);
		setRefreshFootViewRatio(mRefreshFootViewRatio);
		if(!isScrollable)
		{
			restoreLayout(height);
			return;
		}
		LayoutParams params = null;
		int scrollX = 0,scrollY = 0,currentScrollDistance = 0;
		if(PULL_DIRECTION_DOWN == mPullDirection )
		{
			params = (LayoutParams) mHeadView.getLayoutParams();
			params.height = height;
			params.topMargin = -height;
			scrollX = 0;
			scrollY = - mHeadViewHeight;
			currentScrollDistance = scrollY;
		}
		else if(PULL_DIRECTION_UP == mPullDirection)
		{
			params = (LayoutParams) mFootView.getLayoutParams();
			params.height = height;
			params.bottomMargin = -height;
			scrollX = 0;
			scrollY = mFootViewHeight;
			currentScrollDistance = scrollY;

		}
		
		if(isPullViewShow)
		{
			mCurrentDistance = currentScrollDistance;
			scrollTo(scrollX, scrollY);
		}
		
	}
	public void isMeasureHeadView(boolean isMeasure)
	{
		isMeasurePullView = isMeasure;
	}
	private int getPullViewHieght(View view)
	{
		return view.getMeasuredHeight();
	}
	
	private void sendMessage(int type,int intParms,int delay)
	{
		Message msg = Message.obtain();
		msg.what = type;
		msg.arg1 = intParms;
		//delaye
		if(delay > 0)
		{
			mHandler.sendMessageDelayed(msg, delay);
		}
		else //no delay
		{
			mHandler.sendMessage(msg);
		}
	}
	
	private void setLayout(boolean isShow)
	{
		int scrollX = 0,scrollY = 0,currentScrollDistance = 0;
		LayoutParams params  = null;
		if(PULL_DIRECTION_DOWN == mPullDirection )
		{
			// 设置topMargin的值为负的mHeadView高度,即将其隐藏在最上方
			params  = (LayoutParams) mHeadView.getLayoutParams();
			params.topMargin = - mHeadViewHeight;
			params.leftMargin = 0;
			scrollX = 0;
			scrollY = -mHeadViewHeight;
			currentScrollDistance = scrollY;
		}
		else if(PULL_DIRECTION_UP == mPullDirection)
		{
			// 设置bottomMargin的值为负的mFootView高度,即将其隐藏在最下方
			params  = (LayoutParams) mFootView.getLayoutParams();
//			params.gravity = Gravity.BOTTOM;
			params.addRule(ALIGN_PARENT_BOTTOM);
			params.bottomMargin =  - mFootViewHeight;
			params.leftMargin = 0;
			scrollX = 0;
			scrollY = mFootViewHeight;
			currentScrollDistance = scrollY;

		}
		
		requestLayout();
		if(isShow)
		{
			isPullViewShow = true;
			distance = mCurrentDistance = currentScrollDistance;
			scrollTo(scrollX, scrollY);
		}
		
	}
	
	private void restoreLayout(int height)
	{
		LayoutParams params = null;
		if(PULL_DIRECTION_DOWN == mPullDirection )
		{
			params = (LayoutParams) mHeadView.getLayoutParams();
			height = height == 0 ? params.height : height;
			params.topMargin = 0;
			params.height = height;
		}
		else if(PULL_DIRECTION_UP == mPullDirection)
		{
			params = (LayoutParams) mFootView.getLayoutParams();
			height = height == 0 ? params.height : height;
			params.bottomMargin = 0;
			params.height = height;

		}
		requestLayout();
		scrollTo(0, 0);
	}
	
	private void setScrollEnable(boolean isEnable)
	{
		if(isEnable)
		{
			setLayout(isPullViewShow);
		}
		else
		{
			restoreLayout(0);
		}
	}
	
	public boolean isTouchPullViewCanScroll()
	{
		return isTouchPullViewCanScroll;
	}

	public void setTouchPullViewCanScroll(boolean isTouchPullViewCanScroll)
	{
		this.isTouchPullViewCanScroll = isTouchPullViewCanScroll;
	}

	private void setViewTouchListener(View rootView)
	{
		rootView.setOnTouchListener(this);
		if(rootView instanceof ViewGroup)
		{
			int childrenCount = ((ViewGroup) rootView).getChildCount();
			for(int i = 0 ;i < childrenCount; i ++)
			{
				View childrenView = ((ViewGroup) rootView).getChildAt(i);
				if(childrenView instanceof ViewGroup)
				{
					setViewTouchListener(childrenView);
				}
				else
				{
					childrenView.setOnTouchListener(this);
				}
			}
		}
	}
	
	/**
	 * 是否启动顶部滚动，返回true启动顶部滚动
	 */
	private boolean isScroll(View contentView,int deltaY,int deltaX,boolean isPullViewShow)
	{
		
		boolean isPullDown = false;
		boolean isUpToListHead = false;
		boolean isDownToListFoot = false;
		if(contentView instanceof AbsListView)
		{
			AbsListView aListView = (AbsListView) contentView;
			View lastChildren = aListView.getChildAt(aListView.getChildCount() - 1);
			isUpToListHead = aListView.getChildCount() <= 0 || (aListView.getFirstVisiblePosition() == 0 && aListView.getChildAt(0).getTop() >= 0);
			isDownToListFoot = (aListView.getChildCount() <= 0 || (aListView.getLastVisiblePosition() == aListView.getCount() - 1 && lastChildren.getBottom() == aListView.getHeight()));
		}
		else if(contentView instanceof RecyclerView)
		{
			RecyclerView rv = (RecyclerView) contentView;
			LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rv.getLayoutManager();
			int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
			int lastCompletelyVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
			int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
			int firstCompletelyVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
			int totalItemCount = linearLayoutManager.getItemCount();

			isUpToListHead = totalItemCount == 0 || firstCompletelyVisibleItem == 0 || rv.getChildCount() <= 0
				||(firstVisibleItem <= 0 && rv.getChildAt(0).getTop() == 0);
			isDownToListFoot = lastCompletelyVisibleItem == totalItemCount ||
					(lastVisibleItem >= totalItemCount && rv.getChildCount() > 0 &&
							rv.getChildAt(rv.getChildCount() -1).getBottom() == rv.getHeight());
		}
		else if(contentView instanceof ScrollView)
		{
			ScrollView sv = (ScrollView) contentView;
			View lastChildren = sv.getChildAt(sv.getChildCount() - 1);
			isUpToListHead = (sv.getChildCount() <= 0 || sv.getChildAt(0).getTop() == 0);
			isDownToListFoot = (sv.getChildCount() <= 0 ||  lastChildren.getBottom() <= getHeight()) ;
		}
		else if(contentView instanceof ViewPager)
		{
			ViewPager vp = (ViewPager) contentView;
			int childCount = vp.getAdapter().getCount();
			int currentIndex = vp.getCurrentItem();
			if(0 <= currentIndex && currentIndex < childCount)
			{
				View child = vp.findViewWithTag(currentIndex);
				if(null != child)
				{
					return isScroll(child,deltaY,deltaX,isPullViewShow);
				}
			}
		}
		else //ViewGroup
		{
			isUpToListHead = isDownToListFoot = true;
		}
		
		if( isUpToListHead && (PULL_TYPE_DOWN == mPullType || PULL_TYPE_BOTH == mPullType))
		{
			mPullDirection = PULL_DIRECTION_DOWN;
			isPullDown = true;
			
		}
		else if(isDownToListFoot && (PULL_TYPE_UP == mPullType || PULL_TYPE_BOTH == mPullType))
		{
			mPullDirection = PULL_DIRECTION_UP;
			isPullDown = false;
		}
		else
		{
			return false;
		}
		
		//如果斜率小于40度视不滑动
		deltaX = deltaX == 0 ? 1 : deltaX;
		if((double)Math.abs(deltaY) / Math.abs(deltaX) < mVerticalSlope)
		{
			return false;
		}
		//防止误操作
		if(2 >= Math.abs(deltaY))
		{
			return false;
		}
		//垂直方向的时候与水平方向处理有所不同，只要pullview已经显示出来就接管touch事件
		if(isPullViewShow)
		{
			return true;
		}
		
		if(isPullDown)
		{
			return  isScrollDirectionRight(deltaY);
		}
		else
		{
			return  isScrollDirectionRight(deltaY) ;
		}
	}
	
	private boolean isScrollDirectionRight(int delta)
	{
		if(PULL_DIRECTION_DOWN == mPullDirection)
		{
			return delta > 0;
		}
		else if(PULL_DIRECTION_UP == mPullDirection)
		{
			return delta < 0;
		}
		return false;
	}
	
	/**
	 * 初始化pullview
	 */
	private void setupView()
	{
		//添加headview
		setupHeadView();
		//添加ContentView
		setupContentView();
		//添加footview
		setupFootView();
	}
	
	public void setContentView(View aContentView)
	{
		mContentView = mContentPullFireView = aContentView;
		setupContentView();
	}
	
	public void setContentView(View aContentView,View aFireView)
	{
		mContentView = aContentView;
		mContentPullFireView = aFireView;
		setupContentView();
	}
	
	private void setupContentView()
	{
		if(null != mContentView)
		{
			mContentView.setId(R.id.prv_content_view);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			params.addRule(BELOW, R.id.prv_head_view);
			params.addRule(ABOVE, R.id.prv_foot_view);
			addView(mContentView, 1,params);
		}

	}
	private void setupHeadView()
	{
		mHeadView = mInflater.inflate(R.layout.pulltorefresh_head,this,false);
		mHeadView.setId(R.id.prv_head_view);
		mHeadViewTV = (TextView) mHeadView.findViewById(R.id.head_tipsTextView);
		mSubHeadViewTV = (TextView) mHeadView.findViewById(R.id.head_lastUpdatedTextView);
		mHeadViewIV = (ImageView) mHeadView.findViewById(R.id.head_arrowImageView);
		mHeadViewPB = (ProgressBar) mHeadView.findViewById(R.id.head_progressBar);
		if(isMeasurePullView)
		{
			measureView(mHeadView);
		}
		setViewTouchListener(mHeadView);
		
		mHeadViewHeight = getPullViewHieght(mHeadView);
		maxHeadViewPullOutHeight = 2 * mHeadViewHeight;
		minRefreshHeadViewHeight = mHeadViewHeight;
		
		if(null != mHeadView.getParent())
		{
			((ViewGroup)mHeadView.getParent()).removeView(mHeadView);
		}
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mHeadViewHeight);
		// 设置topMargin的值为负的mPullView高度,即将其隐藏在最上方
		params.topMargin = - mHeadViewHeight;
		params.addRule(ALIGN_PARENT_TOP);
		params.leftMargin = 0;
		addView(mHeadView,0, params);
	}
	
	private void setupFootView()
	{
		mFootView = mInflater.inflate(R.layout.pulltorefresh_foot,this,false);
		mFootView.setId(R.id.prv_foot_view);
		mFootViewIV = (ImageView) mFootView.findViewById(R.id.foot_arrowImageView);
		mFootViewPB = (ProgressBar) mFootView.findViewById(R.id.foot_progressBar);
		mFootViewTV = (TextView) mFootView.findViewById(R.id.foot_tipsTextView);
		mSubFootViewTV = (TextView) mFootView.findViewById(R.id.foot_lastUpdatedTextView);
		if(isMeasurePullView)
		{
			measureView(mFootView);
		}
		setViewTouchListener(mFootView);
		
		maxFootViewPullOutHeight = mFootViewHeight = getPullViewHieght(mFootView);
		minRefreshFootViewHeight = mFootViewHeight;
	
		if(null != mFootView.getParent())
		{
			((ViewGroup)mFootView.getParent()).removeView(mFootView);
		}
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mFootViewHeight);
		// 设置bottomMargin的值为负的mPullView高度,即将其隐藏在最下方
//		params.gravity = Gravity.BOTTOM;
		params.addRule(ALIGN_PARENT_BOTTOM);
		params.bottomMargin =  - mFootViewHeight;
		params.leftMargin = 0;
//		params.addRule(BELOW, R.id.list_view);
		addView(mFootView, params);
	
	}
	
	public boolean isPullViewShow()
	{
		return isPullViewShow;
	}
	
	public boolean isRefreshIng()
	{
		return isFootViewRefreshing || isHeadViewRefreshing;
	}
	
	public boolean isHeadViewRefreshing()
	{
		return isHeadViewRefreshing;
	}
	
	public boolean isFootViewRefreshing()
	{
		return isFootViewRefreshing;
	}
	
	/**
	 * 设置刷新类型。（仅下拉刷新，仅上拉刷新，同时上拉下拉刷新，无）
	 * @param type 刷新类型
	 */
	public void setPullRefreshType(int type)
	{
		mPullType = type;
	}
	
	/**
	 * 设置刷新listview的headview的最小高度
	 * @param gap 下拉距离超过此高度将显示“释放刷新”
	 */
	public void setRefreshMinHeadViewGap(int gap)
	{
		if(mHeadViewHeight > gap)
		{
			minRefreshHeadViewHeight = gap;
		}	
	}
	
	/**
	 * 设置刷新listview的footview的最小高度
	 * @param gap 上拉距离超过此高度将显示“释放刷新”
	 */
	public void setRefreshMinFootViewGap(int gap)
	{
		if(mFootViewHeight > gap)
		{
			minRefreshFootViewHeight = gap;
		}	
	}
	/**
	 * 设置刷新listview的headview的最小高度
	 * @param ratio 下拉距离超过此高度(ratio*headViewHeight)将显示“释放刷新”
	 */
	public void setRefreshHeadViewRatio(double ratio)
	{
		mRefreshHeadViewRatio = ratio;
		minRefreshHeadViewHeight = (int)(mHeadViewHeight * ratio);
		minRefreshHeadViewHeight = Math.max(maxHeadViewPullOutHeight, minRefreshHeadViewHeight);
	}
	
	/**
	 * 设置刷新listview的footview的最小高度
	 * @param ratio 下拉距离超过此高度(ratio*footViewHeight)将显示“释放刷新”
	 */
	public void setRefreshFootViewRatio(double ratio)
	{
		mRefreshFootViewRatio = ratio;
		minRefreshFootViewHeight = (int)(mFootViewHeight * ratio);
		minRefreshFootViewHeight = Math.max(maxFootViewPullOutHeight, minRefreshFootViewHeight);
	}
	
	/**
	 * 是否支持上下拉刷新的特性[默认为true]
	 * @param isScrollable
	 */
	public void setScrollable(boolean isScrollable) 
	{
		if(this.isScrollable != isScrollable)
		{
			this.isScrollable = isScrollable;
			setScrollEnable(isScrollable);
		}
	}
	
	/**
	 * 是否支持滑动[默认为true]
	 * @param isScrollable 是否支持滑动
	 * @param isSetEnable 同时设置是否支持滑动特性（如果true，当isScrollable为false时，控件将不再有滑动特性）
	 */
	public void setScrollable(boolean isScrollable,boolean isSetEnable) 
	{
		if(isSetEnable)
		{
			setScrollable(isScrollable);
		}
		else
		{
			this.isScrollable = isScrollable;
		}
	}
	
	public void setmScrollInterface(ScrollInsterface mScrollInterface) 
	{
		this.mScrollInterface = mScrollInterface;
	}
	
	/**
	 * 显示或隐藏pullview
	 * @param isShow
	 */
	public void showPullContentView(boolean isShow)
	{
		if(isPullViewShow)
		{
			if(isShow)
			{
				return;
			}
			else
			{
				isForceHidden = true;
				showPullView(SHOW_HEADER_TYPE_FORCE_HIDDEN);
			}
		}
		else 
		{
			if(isShow)
			{
				isForceShow = true;
				showPullView(SHOW_HEADER_TYPE_FORCE_SHOW);
			}
			else
			{
				return;
			}
		}
	}
	private boolean checkContentIsEmpty()
	{
		if(mContentPullFireView instanceof AbsListView)
		{
			AbsListView absView = (AbsListView) mContentPullFireView;
			View emptyView = absView.getEmptyView();
			if(null != emptyView)
			{
				return emptyView.isShown();
			}
			else
			{
				return false;
			}
		}
		return false;
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) 
	{
		if(!isScrollable || isForceHidden || isForceShow || mPullType == PULL_TYPE_NULL || checkContentIsEmpty())
		{
			return false;
		}
		
		int y = (int) e.getRawY();
		int x = (int) e.getRawX();
		switch (e.getAction()) 
		{
		case MotionEvent.ACTION_DOWN:
			// 首先拦截down事件,记录y,x坐标
			mLastDownY = e.getY();
			mLastMotionY = y;
			mLastMotionX = x;
			isTouchOnPullView = false;
			mScrollState = SCROLL_STATE_UNKNOW;
			break;
		case MotionEvent.ACTION_MOVE:
			if(0 == mLastDownY)
			{
				mLastDownY = e.getY();
			}
			boolean isScroll = isScroll(e);
			if(isScroll)
			{
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mLastMotionY = 0;
			mLastMotionX = 0;
			break;
		}
		return false;	
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(isForceHidden || isForceShow || !isScrollable || isTouchOnPullView ||  mPullType == PULL_TYPE_NULL || checkContentIsEmpty())
		{
			return super.onTouchEvent(event);
		}
		//如果外部有touch事件接管则交由外部接管去做
		if(null != mOuterTouchDelegate)
		{
			mLastDownY = 0;
			return mOuterTouchDelegate.onTouchEvent(event);
		}
		int y = (int) event.getRawY();
		int x = (int) event.getRawX();
		switch (event.getAction()) 
		{
		case MotionEvent.ACTION_DOWN:
			//如果在onInterceptTouchEvent返回true，以后将不会在进入onInterceptTouchEvent，所以
			//在这里再次记录起始位置
			mLastDownY = event.getY();
			mLastMotionY = y;
			mLastMotionX = x;
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			movePostCheck();
			mLastDownY = 0;
			mLastMotionY = 0;
			mLastMotionX = 0;
			showPullView(mShowPullViewType);
			break;

		case MotionEvent.ACTION_MOVE:
			if(mScrollState == SCROLL_STATE_ENABLE)
			{
				handleMoveEvent(event);
			}
			else
			{
				if(isScroll(event))
				{
					handleMoveEvent(event);
				}
			}
			break;
		}
		return true;
	}
	
	boolean isScroll(MotionEvent event)
	{
		int y = (int) event.getRawY();
		int x = (int) event.getRawX();
		// deltaY > 0 是向下运动,< 0是向上运动.并通过外部回调来确定当前是否需要拦截move事件
		int deltaY = y - mLastMotionY;
		int deltaX = x - mLastMotionX;
		boolean isScroll = isScroll(mContentPullFireView,deltaY, deltaX, isPullViewShow);
		if(isScroll && !isTouchOnPullView)
		{
			mScrollState = SCROLL_STATE_ENABLE;
			return true;
		}
		else
		{
			mScrollState = SCROLL_STATE_DISENABLE;
			return false;
		}
	}
	
	void handleMoveEvent(MotionEvent event)
	{
		mScroller.forceFinished(true);
		distance  = (int) (mLastDownY - event.getY());
		distance /= 2; //为了减小滑动的灵敏度
		distance += mCurrentDistance;
		if(distance >= 0) //向上拉
		{
			if(PULL_DIRECTION_UP == mPullDirection)
			{
				//设置向上拖动的最大公高度
//				distance = Math.min(distance,1 * maxFootViewPullOutHeight);
				scroll(0, distance);
			}
			else if(PULL_DIRECTION_DOWN == mPullDirection)
			{
				distance = 0;
				scroll(0, 0); //将当前画布左上角坐标移到0,0位置
				isHeadViewShow = false;
				return;
			}
			
		}
		else //向下拉
		{
			if(PULL_DIRECTION_DOWN == mPullDirection)
			{
				//设置向下拖动的最大公高度
//				distance = Math.max(distance,-1 * maxHeadViewPullOutHeight);
				scroll(0, distance);
			}
			else if(PULL_DIRECTION_UP == mPullDirection)
			{
				distance = 0;
				scroll(0, 0); //将当前画布左上角坐标移到0,0位置
				isFootViewShow = false;
				return;
			}
		}
		
		movePostCheck();
	}	
	
	void scroll(int x,int y)
	{
		if(isShowMovePath)
		{
			scrollTo(x,y);
		}
	}
	private void  movePostCheck()
	{
		if((isHeadViewRefreshing && PULL_DIRECTION_DOWN == mPullDirection)
				|| isFootViewRefreshing && PULL_DIRECTION_UP == mPullDirection)
		{
			return;
		}
		boolean tempIsCanRefreshListView = false;
		int gap = distance;
		int tempMinHeight = PULL_DIRECTION_DOWN == mPullDirection ? minRefreshHeadViewHeight : minRefreshFootViewHeight;
		if (Math.abs(gap) <= tempMinHeight) 
		{
			tempIsCanRefreshListView = false;
		}
		else
		{
			tempIsCanRefreshListView = true;
		}
		if(isCanRefreshListView != tempIsCanRefreshListView)
		{
			onReleaseRefresh(tempIsCanRefreshListView);
		}
		isCanRefreshListView = tempIsCanRefreshListView;
	
	}
	
	void showPullView(int type) 
	{
		boolean isRefreshBegin = false;
//		boolean isFootViewRefreshBegin = false;
		mScroller.forceFinished(true);
		int gap = distance;
		if(SHOW_HEADER_TYPE_FORCE_HIDDEN == type)
		{
			startScroll(0,gap, 0, -gap, mDuration,true);
			isRefreshBegin = false;
			mCurrentDistance = 0;
			sendMessage(100,0,mDuration);
		}
		else if(SHOW_HEADER_TYPE_AUTO == type)
		{
			if(PULL_DIRECTION_DOWN == mPullDirection)
			{
				// 下拉高度没有超过显示刷新listview的最小高度，隐藏headView，并且不触发刷新事件
				if (Math.abs(gap) <= minRefreshHeadViewHeight) 
				{
					startScroll(0,gap, 0, -gap, mDuration,isShowMovePath);
					isRefreshBegin = false;
					isHeadViewShow = false;
					mCurrentDistance = 0;
				}
				else //下拉高度超过显示刷新listview的最小高度，回退headview到触发刷新的最小高度，并且触发刷新事件
				{
					startScroll(0,gap, 0, -gap - minRefreshHeadViewHeight, mDuration,isShowMovePath);
					isRefreshBegin = true;
					isHeadViewShow = true;
					mCurrentDistance = - minRefreshHeadViewHeight;
				}
			}
			else 
			{
				// 上拉的高度没有超过显示刷新listview的最小高度，隐藏footView，并且不触发刷新事件
				if (Math.abs(gap) <= minRefreshFootViewHeight) 
				{
					startScroll(0,gap, 0, -gap, mDuration,isShowMovePath);
					isRefreshBegin = false;
					isFootViewShow = false;
					mCurrentDistance = 0;
				}
				else //上拉高度超过显示刷新listview的最小高度，回退footView到触发刷新的最小高度，并且触发刷新事件
				{
					startScroll(0,gap, 0, minRefreshFootViewHeight -gap, mDuration,isShowMovePath);
					isRefreshBegin = true;
					isFootViewShow = true;
					mCurrentDistance = minRefreshFootViewHeight;
				}
			}
		}
		
		distance = mCurrentDistance;

		if(PULL_DIRECTION_DOWN == mPullDirection)
		{
			if(!isHeadViewRefreshing && isRefreshBegin)
			{
				isHeadViewRefreshing = true;
				mHeadViewTV.setText(R.string.common_pullview_refreshing_label);
				mHeadViewIV.clearAnimation();
				mHeadViewIV.setVisibility(View.GONE);
				mHeadViewPB.setVisibility(View.VISIBLE);
				sendMessage(MSG_WAHT_ON_REFRESH,mPullDirection,mDuration);
			}
			
		}
		else if(PULL_DIRECTION_UP == mPullDirection)
		{
			if(!isFootViewRefreshing && isRefreshBegin)
			{
				isFootViewRefreshing = true;
				mFootViewTV.setText(R.string.common_pullview_loading_label);
				mFootViewIV.clearAnimation();
				mFootViewIV.setVisibility(View.GONE);
				mFootViewPB.setVisibility(View.VISIBLE);
				sendMessage(MSG_WAHT_ON_REFRESH,mPullDirection,mDuration);
			}
			
		}
		isPullViewShow = isRefreshBegin;
		invalidate();//刷新
	}
	void startScroll(int startX, int startY, int dx, int dy, int duration,boolean isShowMovePath)
	{
		if(isShowMovePath)
		{
			mScroller.startScroll(startX,startY, dx ,dy, duration);
		}
	}
	void onPreHidden(boolean isFirst)
	{
		
	}
	void onReleaseRefresh(boolean isRefresh)
	{
		if(PULL_DIRECTION_DOWN == mPullDirection)
		{
			mHeadViewIV.setVisibility(View.VISIBLE);
			mHeadViewIV.clearAnimation();
			if(isRefresh)
			{
				mHeadViewIV.startAnimation(mHeadAnimation);
				mHeadViewTV.setText(R.string.common_pullview_pulldownload_label);
				sendMessage(MSG_WAHT_ON_PRE_REFRESH,mPullDirection, 0);
			}
			else
			{
				mHeadViewIV.startAnimation(mHeadReverseAnimation);
				mHeadViewTV.setText(getPullViewText());
			}
		}
		else
		{
			mFootViewIV.setVisibility(View.VISIBLE);
			mFootViewIV.clearAnimation();
			if(isRefresh)
			{
				mFootViewIV.startAnimation(mFootAnimation);
				mFootViewTV.setText(R.string.common_pullview_pullupload_label);
				sendMessage(MSG_WAHT_ON_PRE_REFRESH,mPullDirection, 0);
			}
			else
			{
				mFootViewIV.startAnimation(mFootReverseAnimation);
				mFootViewTV.setText(getPullViewText());
			}
		}

	}
	String getPullViewText()
	{
		if(PULL_DIRECTION_DOWN == mPullDirection)
		{
			return getContext().getResources().getString(R.string.common_pullview_pulldownrefresh_label);
		}
		else
		{
			return getContext().getResources().getString(R.string.common_pullview_pulluprefresh_label);
		}
	}
	@Override
	public void computeScroll() 
	{
		if (mScroller.computeScrollOffset()) 
		{
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			invalidate();// 刷新
		}
	}

	public long getDuration()
	{
		return mDuration;
	}
	public void setRefreshComplete(int type)
	{
		if(isHeadViewShow)
		{
			String text = String.format(getContext().getString(R.string.common_pullview_refresh_time)
					,DateFormat.format("yyyy-MM-dd hh:mm:ssaa", System.currentTimeMillis()));
			setSubHeadViewText(text);
		}
		switch(type)
		{
		case PULL_TYPE_UP:
			isFootViewRefreshing = false;
			mFootViewPB.setVisibility(View.INVISIBLE);
			if(isFootViewShow)
			{
				isForceHidden = true;
				showPullView(SHOW_HEADER_TYPE_FORCE_HIDDEN);
			}
			break;
		case PULL_TYPE_DOWN:
			isHeadViewRefreshing = false;
			mHeadViewPB.setVisibility(View.INVISIBLE);
			if(isHeadViewShow)
			{
				isForceHidden = true;
				showPullView(SHOW_HEADER_TYPE_FORCE_HIDDEN);
			}
			break;
		case PULL_TYPE_BOTH:
			isHeadViewRefreshing = false;
			isFootViewRefreshing = false;
			mHeadViewPB.setVisibility(View.INVISIBLE);
			mFootViewPB.setVisibility(View.INVISIBLE);
			isForceHidden = true;
			showPullView(SHOW_HEADER_TYPE_FORCE_HIDDEN);
			break;
		}
		
	}
	public void setMyTouchDelegate(OuterTouchDelegate touchDelegate)
	{
		mOuterTouchDelegate = touchDelegate;
	}

	/**
	 * 设置动画的时间
	 * @param mDuration
	 */
	public void setDuration(int mDuration)
	{
		this.mDuration = mDuration;
	}
	
	public int getmPullDirection()
	{
		return mPullDirection;
	}

	/**
	 * 设置pullview滑动的方向
	 * @param mPullDirection
	 */
	public void setmPullDirection(int mPullDirection)
	{
		this.mPullDirection = mPullDirection;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		if(isTouchPullViewCanScroll)
		{
			isTouchOnPullView = false;
			return false;
		}
		isTouchOnPullView = true;
		return false;
	}

	public View getContentView()
	{
		return mContentView;
	}
	
	public View getContentFireView()
	{
		return mContentPullFireView;
	}
	
	/*
	@Deprecated
	public void setEmptyView(int layoutId)
	{
		View emptyView = mInflater.inflate(layoutId, null);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));    
		emptyView.setVisibility(View.GONE);  
		addView(emptyView);  
		mListView.setEmptyView(emptyView);  
	}
	*/
	public void setSubHeadViewText(String subText)
	{
		mSubHeadViewTV.setText(subText);
	}
	
	public void setSubFootViewText(String subText)
	{
		mSubFootViewTV.setText(subText);
	}
	
	public void setSubFootViewVisible(int visibility)
	{
		mSubFootViewTV.setVisibility(visibility);
	}
}

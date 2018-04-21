#### StarRatingBar的属性

    <declare-styleable name="StarRatingBar">
        <!-- 默认的星星图片 优先级高于绘制的星星 -->
        <attr name="defaultStar" format="reference"/>
        <!-- 选中的星星图片 -->
        <attr name="star" format="reference"/>
        <!-- 默认的星星颜色 -->
        <attr name="defaultStarColor" format="color"/>
        <!-- 选中的星星颜色 -->
        <attr name="starColor" format="color"/>
        <!-- 星星的个数 -->
        <attr name="starNum" format="integer"/>
        <!-- 可选的星星的步长 如：0.5表示可选半颗星 -->
        <attr name="starStep" format="float"/>
        <!-- 星星之间的间距 -->
        <attr name="starGap" format="dimension"/>
        <!-- 星星的大小 -->
        <attr name="starSize" format="dimension"/>
        <!-- 选中星星的部分 小于等于starNum -->
        <attr name="rating" format="float"/>
        <!-- 星星是否响应事件 默认为true 表示仅作为展示 -->
        <attr name="isIndicator" format="boolean"/>
    </declare-styleable>
    
#### 实例：

使用绘制的星星样式：

    <com.sxu.starratingbar.StarRatingBar
        android:id="@+id/rating_bar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:defaultStarColor="#999999"
        app:starColor="#ffbb00"
        app:starSize="32dp"
        app:starGap="6dp"/>

使用图片的星星样式:

    <com.sxu.starratingbar.StarRatingBar
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        app:defaultStar="@mipmap/star_unselected_icon"
        app:star="@mipmap/star_selected_icon"
        app:starSize="24dp"
        app:starGap="6dp"/>

代码运行结果：

![image](http://od186sz8s.bkt.clouddn.com/StarRatingBar.jpg)
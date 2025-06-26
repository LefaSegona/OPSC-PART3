import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

object ChartUtils {

    fun setupCategoryBarChart(chart: BarChart, categoryData: Map<String, Float>) {
        // Prepare data entries
        val entries = ArrayList<BarEntry>()
        val categories = ArrayList<String>()

        var index = 0f
        categoryData.forEach { (category, amount) ->
            entries.add(BarEntry(index, amount))
            categories.add(category)
            index++
        }

        // Create dataset with budget-specific colors
        val dataSet = BarDataSet(entries, "Expenses by Category").apply {
            colors = getBudgetColors(categoryData.size)
            valueTextColor = android.R.color.white
            valueTextSize = 12f
            setDrawValues(true) // Show values on bars
        }

        // Configure chart appearance
        chart.apply {
            data = BarData(dataSet)
            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(categories)
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                setDrawGridLines(false)
                textColor = android.R.color.white
            }

            axisLeft.apply {
                textColor = android.R.color.white
                axisMinimum = 0f
                granularity = 100f
            }

            axisRight.isEnabled = false
            legend.textColor = android.R.color.white
            description.isEnabled = false
            setTouchEnabled(true)
            setPinchZoom(true)

            // Animation
            animateY(1000)
            invalidate()
        }
    }

    private fun getBudgetColors(count: Int): List<Int> {
        return when {
            count <= 3 -> listOf(
                android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light
            )
            count <= 6 -> ColorTemplate.MATERIAL_COLORS.toList()
            else -> ColorTemplate.VORDIPLOM_COLORS.toList()
        }
    }
}
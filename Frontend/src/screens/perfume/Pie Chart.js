import React, { Component } from 'react';
import CanvasJSReact from '../../assets/canvasjs.react';
var CanvasJSChart = CanvasJSReact.CanvasJSChart;

class PieChart extends Component {

	render() {
		const sum = 5;
		
		const datas = this.props.data;

		const options = {
			exportEnabled: false,
			animationEnabled: false,
			data: [{
				type: "pie",
				startAngle: 75,
				toolTipContent: "<b>{accordClassName}</b>: {y}%",
				showInLegend: "true",
				legendText: "{accordClassName}",
				indexLabelFontSize: 16,
				indexLabel: "{accordClassName} - {y}%",
				dataPoints: datas
			}]
		}

		return (
		<div>
			<CanvasJSChart options = {options} 
				/* onRef={ref => this.chart = ref} */
			/>
			{/*You can get reference to the chart instance as shown above using onRef. This allows you to access all chart properties and methods*/}
		</div>
		);
	}
}

export default PieChart;
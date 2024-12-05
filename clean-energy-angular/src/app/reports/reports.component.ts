import { Component } from '@angular/core';
import { SalesService } from 'src/services/sales-service/sales.service';
import * as d3 from 'd3';


@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.css']
})
export class ReportsComponent {

       
      salesData:any;

       constructor(private salesService:SalesService) {
          this.fetchSalesData();
       }
       
        fetchSalesData(){
          this.salesService.fetchSalesData().then((response) => {
            this.salesData = response;
            this.createChart(response);
            console.log("Sales data:", response);
          }).catch((error) => {
            console.error("Error fetching sales data:", error);
          });
        }

        private createChart(data: any[]): void {
          const width = 500, height = 300, margin = 50;
      
          const svg = d3.select('#chart')
              .append('svg')
              .attr('width', width + margin * 2)
              .attr('height', height + margin * 2)
              .append('g')
              .attr('transform', `translate(${margin}, ${margin})`);
      
          const x = d3.scaleBand()
              .domain(data.map(d => d.productName))
              .range([0, width])
              .padding(0.1);
      
          const y = d3.scaleLinear()
              .domain([0, d3.max(data, d => d.productPrice)!])
              .range([height, 0]);
      
          // Add bars
          svg.selectAll('rect')
              .data(data)
              .enter()
              .append('rect')
              .attr('x', d => x(d.productName)!)
              .attr('y', d => y(d.productPrice))
              .attr('width', x.bandwidth())
              .attr('height', d => height - y(d.productPrice))
              .attr('fill', 'steelblue');
      
          // Add X axis
          svg.append('g')
              .attr('transform', `translate(0,${height})`)
              .call(d3.axisBottom(x))
              .selectAll('text')
              .style('text-anchor', 'middle')
              .style('font-size', '12px')
              .attr('transform', 'rotate(-45)')
              .style('fill', 'black');
      
          // Add Y axis
          svg.append('g')
              .call(d3.axisLeft(y))
              .style('fill', 'black')
              .style('font-size', '12px');
      
          // Add labels above bars
          svg.selectAll('.label')
              .data(data)
              .enter()
              .append('text')
              .attr('class', 'label')
              .attr('x', d => x(d.productName)! + x.bandwidth() / 2) // Centered above the bar
              .attr('y', d => y(d.productPrice) - 5) // Just above the bar
              .attr('text-anchor', 'middle')
              .text(d => d.productPrice) // Display the product price
              .style('fill', 'black') // Text color
              .style('font-size', '10px'); // Adjust font size
      }
      
        
      

}

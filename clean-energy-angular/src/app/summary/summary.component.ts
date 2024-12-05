import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import * as d3 from 'd3';
import { SalesService } from 'src/services/sales-service/sales.service';

@Component({
  selector: 'app-summary',
  templateUrl: './summary.component.html',
  styleUrls: ['./summary.component.css']
})
export class SummaryComponent implements OnInit {
  // Holds the list of product categories
  categories: string[] = [];
  // The currently selected product category
  selectedCategory: string = '';
  // The sales data corresponding to the selected category
  salesData: any[] = [];

  // Inject the SalesService for API calls and ChangeDetectorRef for manual change detection
  constructor(private salesService: SalesService, private cdr: ChangeDetectorRef) {}

  // Lifecycle hook that runs when the component initializes
  ngOnInit(): void {
    this.loadProductCategories(); // Load product categories when the component is initialized
  }

  /**
   * Fetches the list of product categories from the server.
   * Updates the `categories` array and triggers change detection.
   */
  loadProductCategories(): void {
    this.salesService.fetchProductCategories()
      .then((response) => {
        this.categories = response; // Assign fetched categories to the array
        this.cdr.detectChanges(); // Ensure Angular detects and updates the changes
      })
      .catch((error) => {
        console.error('Error fetching product categories:', error); // Log any errors
        this.categories = []; // Fallback to an empty array if there's an error
      });
  }

  /**
   * Fetches sales data for the selected product category.
   * Waits for the data and then renders the chart.
   */
  async fetchSalesData(): Promise<void> {
    if (!this.selectedCategory) {
      console.warn('No category selected.'); // Warn if no category is selected
      return; // Exit the function if no category is selected
    }

    try {
      // Fetch sales data for the selected category
      this.salesData = await this.salesService.fetchSalesDatawithProductCategory(this.selectedCategory);
      // Allow the DOM to update before rendering the chart
      await new Promise(resolve => setTimeout(resolve, 0));
      this.renderChart(); // Render the chart using the fetched data
      this.cdr.detectChanges(); // Ensure Angular detects and updates the changes
    } catch (error) {
      console.error('Error fetching sales data:', error); // Log any errors
      this.salesData = []; // Fallback to an empty array if there's an error
    }
  }

  /**
   * Renders a bar chart using D3.js based on the `salesData`.
   */
  renderChart(): void {
    // Get the chart container element
    const chartContainer = document.getElementById('chart');
    if (!chartContainer) {
      console.error('Chart container not found'); // Log error if container is missing
      return; // Exit the function if the container is not found
    }

    // Clear any existing content in the chart container
    d3.select(chartContainer).selectAll('*').remove();

    // Handle the case where there is no sales data
    if (this.salesData.length === 0) {
      d3.select(chartContainer).append('p').text('No sales data available.'); // Display a message
      return;
    }

    // Dimensions and margins for the chart
    const width = 500;
    const height = 300;
    const margin = { top: 20, right: 30, bottom: 50, left: 40 };

    // Use requestAnimationFrame to ensure the DOM is ready before creating the chart
    requestAnimationFrame(() => {
      // Create an SVG element inside the chart container
      const svg = d3.select(chartContainer)
        .append('svg')
        .attr('width', width)
        .attr('height', height);

      // Define the X scale (categories on the horizontal axis)
      const xScale = d3.scaleBand()
        .domain(this.salesData.map(d => d.productName)) // Use product names as categories
        .range([margin.left, width - margin.right]) // Scale within the chart width
        .padding(0.1); // Add padding between bars

      // Define the Y scale (prices on the vertical axis)
      const yScale = d3.scaleLinear()
        .domain([0, d3.max(this.salesData, d => d.productPrice) as number]) // Max price as the upper limit
        .nice() // Make the axis labels user-friendly
        .range([height - margin.bottom, margin.top]); // Scale within the chart height

      // Draw the bars
      svg.selectAll('.bar')
        .data(this.salesData) // Bind the sales data
        .enter()
        .append('rect') // Add a rectangle for each data point
        .attr('class', 'bar')
        .attr('x', d => xScale(d.productName)!) // Position the bar horizontally
        .attr('y', d => yScale(d.productPrice)) // Position the bar vertically
        .attr('width', xScale.bandwidth()) // Set the bar width
        .attr('height', d => height - margin.bottom - yScale(d.productPrice)) // Set the bar height
        .attr('fill', 'steelblue'); // Set the bar color

      // Add the X-axis
      svg.append('g')
        .attr('transform', `translate(0,${height - margin.bottom})`) // Position the axis at the bottom
        .call(d3.axisBottom(xScale)) // Create the axis
        .selectAll('text')
        .attr('transform', 'rotate(-45)') // Rotate the labels for better readability
        .style('text-anchor', 'end'); // Align the text to the end

      // Add the Y-axis
      svg.append('g')
        .attr('transform', `translate(${margin.left},0)`) // Position the axis on the left
        .call(d3.axisLeft(yScale)); // Create the axis
    });
  }
}

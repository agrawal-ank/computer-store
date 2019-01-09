package com.tcs.salesmgmt.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.salesmgmt.domain.model.CSVItem;
import com.tcs.salesmgmt.domain.model.CommonProperty;
import com.tcs.salesmgmt.domain.model.ItemDetails;
import com.tcs.salesmgmt.domain.model.ManufactureDetails;
import com.tcs.salesmgmt.domain.model.Product;
import com.tcs.salesmgmt.domain.model.ProductProperties;
import com.tcs.salesmgmt.domain.model.ProductType;
import com.tcs.salesmgmt.domain.repo.ManufacturerRepository;
import com.tcs.salesmgmt.domain.repo.ProductItemRepository;

@Service
public class ProductItemService {
	@Autowired
	ProductItemRepository productItemRepository;

	@Autowired
	ManufacturerRepository manufacturerRepository;

	@Transactional
	public void saveOrUpdateCSVItemList(List<CSVItem> csvItemList) {
		for (CSVItem csvItem : csvItemList) {
			final String productType = csvItem.getProductType();
			final int amount = csvItem.getAmount();

			Map<String, String> itemProperties = csvItem.getItemProperties();
			final String seriesNumber = itemProperties.get(CommonProperty.SERIES_NUMBER.name());
			final String manufacturer = itemProperties.get(CommonProperty.MANUFACTURER.name());
			final double price = Double.valueOf(itemProperties.get(CommonProperty.PRICE.name()));
			final String productPropertyName = Product.valueOf(productType.toUpperCase()).getProperty();
			final String productPropertyValue = itemProperties.get(productPropertyName);

			ProductType productItemDetails = productItemRepository.findByProductType(productType);
			if (productItemDetails == null) {
				productItemDetails = new ProductType();
				productItemDetails.setProductType(productType);
			}

			List<ProductProperties> productPropertiesList = productItemDetails.getProductProperties();
			ProductProperties productProperty = productPropertiesList.stream()
					.filter(each -> each.getPropertyValue().equals(productPropertyValue)).findFirst().orElse(null);
			if (productProperty == null) {
				productProperty = new ProductProperties();
				productProperty.setPropertyName(productPropertyName);
				productProperty.setPropertyValue(productPropertyValue);

				productPropertiesList.add(productProperty);
			}

			List<ManufactureDetails> manufacturerList = productProperty.getManufacturedBy();
			ManufactureDetails manufacturerDetails = manufacturerList.stream()
					.filter(each -> each.getManufacturerName().equals(manufacturer)).findFirst().orElse(null);
			if (manufacturerDetails == null) {
				manufacturerDetails = manufacturerRepository.findByManufacturerName(manufacturer);
				if (manufacturerDetails == null) {
					manufacturerDetails = new ManufactureDetails();
					manufacturerDetails.setManufacturerName(manufacturer);
				}
				manufacturerList.add(manufacturerDetails);
			}

			List<ItemDetails> itemDetailsList = manufacturerDetails.getItemDetails();
			ItemDetails itemDetails = itemDetailsList.stream()
					.filter(each -> each.getSeriesNumber().equals(seriesNumber)).findFirst().orElse(null);
			if (itemDetails == null) {
				itemDetails = new ItemDetails();
				itemDetails.setSeriesNumber(seriesNumber);
				itemDetails.setPrice(price);
				itemDetails.setAmount(amount);
				itemDetails.setProductProperties(productProperty);

				itemDetailsList.add(itemDetails);
			} else {
				itemDetails.setAmount(itemDetails.getAmount() + amount);
			}

			productItemRepository.save(productItemDetails);
		}
	}

	@Transactional
	public String getItemDetails() {
		final String lineSeperator = System.lineSeparator();

		StringBuilder treeViewSB = new StringBuilder();

		/*
		 * Retrieving all available product types (computer, laptop etc)
		 */
		for (ProductType item : productItemRepository.findAll()) {
			treeViewSB.append(item.getProductType() + lineSeperator);

			/*
			 * Retrieving sub-types(e.g. form-factor of each product type (computer, laptop etc) 
			 */
			for (ProductProperties properties : item.getProductProperties()) {
				treeViewSB.append("\t" + properties.getPropertyValue() + lineSeperator);

				/*
				 * Retrieving manufacturer details for each product and its sub-type 
				 */
				for (ManufactureDetails manufacturer : properties.getManufacturedBy()) {
					treeViewSB.append("\t\t" + manufacturer.getManufacturerName() + lineSeperator);

					/*
					 * Retrieving series details (number, price, amount) for each manufacturer as per product type (sub-type)
					 */
					for (ItemDetails itemDetails : manufacturer.getItemDetails()) {
						if (itemDetails.getProductProperties().equals(properties)) {
							treeViewSB.append("\t\t\t[ " + itemDetails.getSeriesNumber() + " | "
									+ itemDetails.getPrice() + " | " + itemDetails.getAmount() + " ]" + lineSeperator);
						}
					}
				}
			}
		}
		return treeViewSB.toString();
	}

	/*
	 * We can use Tree Data Structure (defined below) to represent all items in tree format.
	 * But avoiding it as Entity Class Design itself representing a hierarchical design.   
	 */
	@Transactional
	public void getItemsTreeFormat() {
		TreeNode root = new TreeNode("Computer Store");

		for (ProductType item : productItemRepository.findAll()) {
			TreeNode productTypeNode = new TreeNode(item.getProductType());
			root.addChildNode(productTypeNode);

			for (ProductProperties properties : item.getProductProperties()) {
				TreeNode productPopertiesNode = new TreeNode(properties.getPropertyValue());
				productTypeNode.addChildNode(productPopertiesNode);

				for (ManufactureDetails manufacturer : properties.getManufacturedBy()) {
					TreeNode manufacturerNode = new TreeNode(manufacturer.getManufacturerName());
					productPopertiesNode.addChildNode(manufacturerNode);

					for (ItemDetails itemDetails : manufacturer.getItemDetails()) {
						if (itemDetails.getProductProperties().equals(properties)) {
							String data = "[ " + itemDetails.getSeriesNumber() + " | " + itemDetails.getPrice() + " | "
									+ itemDetails.getAmount() + " ]";

							TreeNode itemNode = new TreeNode(data);
							manufacturerNode.addChildNode(itemNode);
						}
					}
				}

			}
		}
		print(root, " ");
	}

	private void print(TreeNode root, String aling) {
		System.out.println(aling + root.getData());
		root.getChildNodes().forEach(each -> print(each, aling + aling));
	}

	class TreeNode {
		private String data;
		private List<TreeNode> childNodes;

		public TreeNode(String data) {
			this.data = data;
			childNodes = new ArrayList<TreeNode>();
		}

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}

		public List<TreeNode> getChildNodes() {
			return childNodes;
		}

		public void setChildNodes(List<TreeNode> childNodes) {
			this.childNodes = childNodes;
		}

		public void addChildNode(TreeNode childNode) {
			this.childNodes.add(childNode);
		}

		@Override
		public String toString() {
			return "[data=" + data + "]";
		}
	}
}

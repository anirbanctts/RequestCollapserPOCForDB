package com.poc.sample.repos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.poc.sample.dtos.CustomDTO;
import com.poc.sample.entity.CaptainCreditLimit;
import com.poc.sample.entity.City;
import com.poc.sample.messages.SearchMessage;
import com.poc.sample.repos.jpa.BookingRepositoryImpl;
import com.poc.sample.repos.jpa.MySampleJPARepo;

@Component
public class MyCaptainCreditRepo {

	@Autowired
	private BookingRepositoryImpl bookingRepositoryImpl;
	
	public List<CustomDTO> getValuesBySearchParam(List<SearchMessage> searchModels)  {
		
		List<Integer> captainIds = searchModels
	            .stream()
	            .map(SearchMessage::getSearchId)
	            .collect(Collectors.toList());
		
		System.out.println("Hitting main method for captainIds: " + captainIds);
		List<CaptainCreditLimit> creditLimits =  bookingRepositoryImpl.getCaptainCreditLimits(captainIds);
		List<CustomDTO> dtos = mapCaptainCreditLimitsToDtos(creditLimits);
		List<CustomDTO> sortedFormat = maintainResponseOrder(dtos, searchModels);
		return sortedFormat;
	}

	private List<CustomDTO> maintainResponseOrder(List<CustomDTO> dtos, List<SearchMessage> searchModels) {
		
		List<CustomDTO> listCorrectOrder = new ArrayList<>();
		
		searchModels.stream().forEach(searchModel -> {
			
			listCorrectOrder.add(dtos.stream().filter(item -> item.getId()==searchModel.getSearchId()).findFirst().get());
		});
		return listCorrectOrder;
	}

	private List<CustomDTO> mapCaptainCreditLimitsToDtos(List<CaptainCreditLimit> captainCreditLimits) {
		List<CustomDTO> dtos = new ArrayList<>();
		captainCreditLimits.stream().forEach(captainCreditLimit-> {
			dtos.add(new CustomDTO() {{ setId(captainCreditLimit.getCaptainId()); setName(captainCreditLimit.getCreditLimit().toString()); }});
		});
		
		return dtos;
	}
}

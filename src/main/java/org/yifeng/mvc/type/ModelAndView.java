package org.yifeng.mvc.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelAndView {
    private String view;

    private Map<String,Object> model=new HashMap<>();

    public ModelAndView setView(String view){
        this.view=view;
        return this;
    }

    public ModelAndView addViewData(String attributeName,Object attributeValue){
        model.put(attributeName,attributeValue);
        return this;
    }

}

package cn.zcw.groupentity;

import cn.zcw.domain.TbSpecification;
import cn.zcw.domain.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

public class Specification implements Serializable {
//    引入规格表对象
    private TbSpecification tbSpecification;
//    引入规格项对象
    private List<TbSpecificationOption> tbSpecificationOptionList;

    public TbSpecification getTbSpecification() {
        return tbSpecification;
    }

    public void setTbSpecification(TbSpecification tbSpecification) {
        this.tbSpecification = tbSpecification;
    }

    public List<TbSpecificationOption> getTbSpecificationOptionList() {
        return tbSpecificationOptionList;
    }

    public void setTbSpecificationOptionList(List<TbSpecificationOption> tbSpecificationOptionList) {
        this.tbSpecificationOptionList = tbSpecificationOptionList;
    }
}

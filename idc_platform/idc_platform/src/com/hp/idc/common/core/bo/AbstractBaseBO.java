package com.hp.idc.common.core.bo;

/**
 * 业务抽象类
 * @ClassName: AbstractBaseBO
 * @Descprition: TODO
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>
 * @version 1.0
 */
public abstract class AbstractBaseBO
{
  public abstract long getId();

  public boolean equals(Object object)
  {
    if (super.getClass().isInstance(object)) {
      AbstractBaseBO bo = (AbstractBaseBO)object;
      if (bo.getId() == getId()) {
        return true;
      }
    }
    return false;
  }

  public int hashCode()
  {
    return ((Long)getId()).intValue();
  }
}
update region_entity
set location_start=?,
    location_end=?,
    display_name=?,
    priority=?,
    flag=?
/*where world_name = ?
  and region_name = ?*/

where id = ?;
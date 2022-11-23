import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LocalAssociationProvinceComponent } from './list/local-association-province.component';
import { LocalAssociationProvinceDetailComponent } from './detail/local-association-province-detail.component';
import { LocalAssociationProvinceUpdateComponent } from './update/local-association-province-update.component';
import { LocalAssociationProvinceDeleteDialogComponent } from './delete/local-association-province-delete-dialog.component';
import { LocalAssociationProvinceRoutingModule } from './route/local-association-province-routing.module';

@NgModule({
  imports: [SharedModule, LocalAssociationProvinceRoutingModule],
  declarations: [
    LocalAssociationProvinceComponent,
    LocalAssociationProvinceDetailComponent,
    LocalAssociationProvinceUpdateComponent,
    LocalAssociationProvinceDeleteDialogComponent,
  ],
  entryComponents: [LocalAssociationProvinceDeleteDialogComponent],
})
export class LocalAssociationProvinceModule {}

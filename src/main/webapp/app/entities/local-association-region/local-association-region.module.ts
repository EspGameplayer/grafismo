import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LocalAssociationRegionComponent } from './list/local-association-region.component';
import { LocalAssociationRegionDetailComponent } from './detail/local-association-region-detail.component';
import { LocalAssociationRegionUpdateComponent } from './update/local-association-region-update.component';
import { LocalAssociationRegionDeleteDialogComponent } from './delete/local-association-region-delete-dialog.component';
import { LocalAssociationRegionRoutingModule } from './route/local-association-region-routing.module';

@NgModule({
  imports: [SharedModule, LocalAssociationRegionRoutingModule],
  declarations: [
    LocalAssociationRegionComponent,
    LocalAssociationRegionDetailComponent,
    LocalAssociationRegionUpdateComponent,
    LocalAssociationRegionDeleteDialogComponent,
  ],
  entryComponents: [LocalAssociationRegionDeleteDialogComponent],
})
export class LocalAssociationRegionModule {}

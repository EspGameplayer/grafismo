import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITeam, Team } from '../team.model';
import { TeamService } from '../service/team.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IFormation } from 'app/entities/formation/formation.model';
import { FormationService } from 'app/entities/formation/service/formation.service';
import { IStadium } from 'app/entities/stadium/stadium.model';
import { StadiumService } from 'app/entities/stadium/service/stadium.service';

@Component({
  selector: 'jhi-team-update',
  templateUrl: './team-update.component.html',
})
export class TeamUpdateComponent implements OnInit {
  isSaving = false;

  formationsSharedCollection: IFormation[] = [];
  stadiumsSharedCollection: IStadium[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    graphicsName: [null, [Validators.required]],
    abb: [null, [Validators.required]],
    badge: [],
    badgeContentType: [],
    monocBadge: [],
    monocBadgeContentType: [],
    preferredFormation: [],
    stadiums: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected teamService: TeamService,
    protected formationService: FormationService,
    protected stadiumService: StadiumService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ team }) => {
      this.updateForm(team);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('grafismoApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const team = this.createFromForm();
    if (team.id !== undefined) {
      this.subscribeToSaveResponse(this.teamService.update(team));
    } else {
      this.subscribeToSaveResponse(this.teamService.create(team));
    }
  }

  trackFormationById(_index: number, item: IFormation): number {
    return item.id!;
  }

  trackStadiumById(_index: number, item: IStadium): number {
    return item.id!;
  }

  getSelectedStadium(option: IStadium, selectedVals?: IStadium[]): IStadium {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeam>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(team: ITeam): void {
    this.editForm.patchValue({
      id: team.id,
      name: team.name,
      graphicsName: team.graphicsName,
      abb: team.abb,
      badge: team.badge,
      badgeContentType: team.badgeContentType,
      monocBadge: team.monocBadge,
      monocBadgeContentType: team.monocBadgeContentType,
      preferredFormation: team.preferredFormation,
      stadiums: team.stadiums,
    });

    this.formationsSharedCollection = this.formationService.addFormationToCollectionIfMissing(
      this.formationsSharedCollection,
      team.preferredFormation
    );
    this.stadiumsSharedCollection = this.stadiumService.addStadiumToCollectionIfMissing(
      this.stadiumsSharedCollection,
      ...(team.stadiums ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.formationService
      .query()
      .pipe(map((res: HttpResponse<IFormation[]>) => res.body ?? []))
      .pipe(
        map((formations: IFormation[]) =>
          this.formationService.addFormationToCollectionIfMissing(formations, this.editForm.get('preferredFormation')!.value)
        )
      )
      .subscribe((formations: IFormation[]) => (this.formationsSharedCollection = formations));

    this.stadiumService
      .query()
      .pipe(map((res: HttpResponse<IStadium[]>) => res.body ?? []))
      .pipe(
        map((stadiums: IStadium[]) =>
          this.stadiumService.addStadiumToCollectionIfMissing(stadiums, ...(this.editForm.get('stadiums')!.value ?? []))
        )
      )
      .subscribe((stadiums: IStadium[]) => (this.stadiumsSharedCollection = stadiums));
  }

  protected createFromForm(): ITeam {
    return {
      ...new Team(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      graphicsName: this.editForm.get(['graphicsName'])!.value,
      abb: this.editForm.get(['abb'])!.value,
      badgeContentType: this.editForm.get(['badgeContentType'])!.value,
      badge: this.editForm.get(['badge'])!.value,
      monocBadgeContentType: this.editForm.get(['monocBadgeContentType'])!.value,
      monocBadge: this.editForm.get(['monocBadge'])!.value,
      preferredFormation: this.editForm.get(['preferredFormation'])!.value,
      stadiums: this.editForm.get(['stadiums'])!.value,
    };
  }
}

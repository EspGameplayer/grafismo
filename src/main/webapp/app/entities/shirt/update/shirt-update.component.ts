import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IShirt, Shirt } from '../shirt.model';
import { ShirtService } from '../service/shirt.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { ISeason } from 'app/entities/season/season.model';
import { SeasonService } from 'app/entities/season/service/season.service';

@Component({
  selector: 'jhi-shirt-update',
  templateUrl: './shirt-update.component.html',
})
export class ShirtUpdateComponent implements OnInit {
  isSaving = false;

  teamsSharedCollection: ITeam[] = [];
  seasonsSharedCollection: ISeason[] = [];

  editForm = this.fb.group({
    id: [],
    colour1: [null, [Validators.pattern('^[0-9a-f]{6}$|^$')]],
    colour2: [null, [Validators.pattern('^[0-9a-f]{6}$|^$')]],
    type: [null, [Validators.min(1), Validators.max(3)]],
    team: [],
    season: [],
  });

  constructor(
    protected shirtService: ShirtService,
    protected teamService: TeamService,
    protected seasonService: SeasonService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shirt }) => {
      this.updateForm(shirt);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const shirt = this.createFromForm();
    if (shirt.id !== undefined) {
      this.subscribeToSaveResponse(this.shirtService.update(shirt));
    } else {
      this.subscribeToSaveResponse(this.shirtService.create(shirt));
    }
  }

  trackTeamById(_index: number, item: ITeam): number {
    return item.id!;
  }

  trackSeasonById(_index: number, item: ISeason): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IShirt>>): void {
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

  protected updateForm(shirt: IShirt): void {
    this.editForm.patchValue({
      id: shirt.id,
      colour1: shirt.colour1,
      colour2: shirt.colour2,
      type: shirt.type,
      team: shirt.team,
      season: shirt.season,
    });

    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, shirt.team);
    this.seasonsSharedCollection = this.seasonService.addSeasonToCollectionIfMissing(this.seasonsSharedCollection, shirt.season);
  }

  protected loadRelationshipsOptions(): void {
    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('team')!.value)))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));

    this.seasonService
      .query()
      .pipe(map((res: HttpResponse<ISeason[]>) => res.body ?? []))
      .pipe(map((seasons: ISeason[]) => this.seasonService.addSeasonToCollectionIfMissing(seasons, this.editForm.get('season')!.value)))
      .subscribe((seasons: ISeason[]) => (this.seasonsSharedCollection = seasons));
  }

  protected createFromForm(): IShirt {
    return {
      ...new Shirt(),
      id: this.editForm.get(['id'])!.value,
      colour1: this.editForm.get(['colour1'])!.value,
      colour2: this.editForm.get(['colour2'])!.value,
      type: this.editForm.get(['type'])!.value,
      team: this.editForm.get(['team'])!.value,
      season: this.editForm.get(['season'])!.value,
    };
  }
}
